package com.hashicorp.hashicraft.nomad;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Job {
    Spec Job;

    public Job(String name, String version, String nomadDeployment) {
        Job = new Spec(name, version, nomadDeployment);
    }

    public static class Spec {

        private static final String NOMAD_DATACENTER = "dc1";
        private static final String NOMAD_NAMESPACE = "default";
        private static final int APPLICATION_PORT = 3000;
        private static final int METRICS_PORT = 9102;
        private static final String CONTAINER_IMAGE = "nicholasjackson/fake-service:v0.24.2";

        private static final String ERROR_VERSION = "red";

        public String Name;
        public String ID;
        public String Namespace = NOMAD_NAMESPACE;
        public List<String> Datacenters = Lists.newArrayList(NOMAD_DATACENTER);
        public List<TaskGroup> TaskGroups;
        public Map<String, String> Meta;


        public Spec(String name, String version, String nomadDeployment) {
            Name = nomadDeployment;
            ID = nomadDeployment;
            TaskGroups = version.contentEquals(ERROR_VERSION) ?
                    Lists.newArrayList(new TaskGroup(name, version, true)) :
                    Lists.newArrayList(new TaskGroup(name, version, false));
            Meta = Map.of("version", version);
        }

        static class TaskGroup {
            String Name;
            int Count = 3;
            List<Network> Networks = Lists.newArrayList(
                    new Network()
            );
            List<Service> Services;
            List<Task> Tasks;

            public TaskGroup(String name, String version, boolean addErrors) {
                Name = name;
                Services = Lists.newArrayList(
                        new Service(name + "-metrics",
                                "metrics",
                                Map.of("metrics", "prometheus",
                                        "datacenter", "${node.datacenter}",
                                        "job", "${NOMAD_JOB_NAME}"),
                                Lists.newArrayList("metrics"), false),
                        new Service(name,
                                String.format("%d", APPLICATION_PORT),
                                null,
                                null,
                                true)
                );
                Tasks = Lists.newArrayList(
                        new Task(name,
                                createEnvironmentVariables(addErrors, version),
                                new Task.Config(
                                        CONTAINER_IMAGE,
                                        Lists.newArrayList("http"),
                                        null),
                                new Task.Resources(500, 128)
                        ),
                        new Task("socat",
                                null,
                                new Task.Config(
                                        "alpine/socat",
                                        null,
                                        Lists.newArrayList(
                                                "TCP-LISTEN:19002,fork,bind=127.0.0.1",
                                                "TCP:127.0.0.2:19002")),
                                null
                        )
                );
            }

            private Map<String, String> createEnvironmentVariables(boolean addErrors, String version) {
                Map<String, String> environmentVariables = new HashMap<>(Map.of(
                        "LISTEN_ADDR", String.format("0.0.0.0:%d", APPLICATION_PORT),
                        "NAME", String.format("%s %s", this.Name, version),
                        "TIMING_50_PERCENTILE", "20ms"));
                if (addErrors) {
                    environmentVariables.put("ERROR_RATE", "0.3");
                }
                return environmentVariables;
            }
        }

        static class Network {
            String Mode = "bridge";
            List<Network.DynamicPort> DynamicPorts = Lists.newArrayList(
                    new Network.DynamicPort("http", APPLICATION_PORT),
                    new Network.DynamicPort("metrics", METRICS_PORT)
            );

            static class DynamicPort {
                String Label;
                int To;

                DynamicPort(String label, int to) {
                    Label = label;
                    To = to;
                }
            }
        }

        static class Service {
            String Name;
            String PortLabel;
            Map<String, String> Meta;
            List<String> Tags;

            Connect Connect;

            Service(String name, String port, Map<String, String> meta, List<String> tags, boolean addConsul) {
                Name = name;
                PortLabel = port;
                Meta = meta;
                Tags = tags;
                Connect = addConsul ? new Connect() : null;
            }

            static class Connect {
                SidecarService SidecarService = new SidecarService();

                static class SidecarService {
                    Proxy Proxy = new Proxy();

                    static class Proxy {
                        Map<String, String> Config = Map.of(
                                "envoy_prometheus_bind_addr", String.format("0.0.0.0:%d", METRICS_PORT));
                    }
                }
            }
        }

        static class Task {
            String Name;
            Config Config;
            String Driver = "docker";
            Map<String, String> Env;
            Resources Resources;


            public Task(String name, Map<String, String> env, Config config, Resources resources) {
                Name = name;
                Env = env;
                Config = config;
                Resources = resources;
            }

            static class Config {
                String image;
                List<String> ports;
                List<String> args;

                public Config(String image, List<String> ports, List<String> args) {
                    this.image = image;
                    this.ports = ports;
                    this.args = args;
                }
            }

            static class Resources {
                int CPU;
                int MemoryMB;

                public Resources(int CPU, int memoryMB) {
                    this.CPU = CPU;
                    MemoryMB = memoryMB;
                }
            }
        }
    }

    public String toJson() {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(this);
    }
}
