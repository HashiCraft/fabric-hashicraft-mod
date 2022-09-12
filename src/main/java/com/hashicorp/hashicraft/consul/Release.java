package com.hashicorp.hashicraft.consul;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class Release {
    public String name;
    public ConsulReleaser releaser;
    public NomadRuntime runtime;
    public CanaryStrategy strategy;
    public PrometheusMonitor monitor;

    public static class ConsulReleaser {
        String plugin_name;
        Map<String, String> config;

        public ConsulReleaser(String consulService) {
            this.plugin_name = "consul";
            this.config = Map.of("consul_service", consulService);
        }
    }

    public static class NomadRuntime {
        String plugin_name;
        NomadConfig config;

        public NomadRuntime(NomadConfig config) {
            this.plugin_name = "nomad";
            this.config = config;
        }
    }

    public static class NomadConfig {
        String deployment;
        String namespace;

        public NomadConfig(String deployment, String namespace) {
            this.deployment = deployment;
            this.namespace = namespace;
        }
    }

    public static class CanaryStrategy {
        String plugin_name;
        Config config;

        static class Config {
            String interval;
            int initial_traffic;
            int traffic_step;
            int max_traffic;
            int error_threshold;

            public Config() {
                this.interval = "30s";
                this.initial_traffic = 10;
                this.traffic_step = 20;
                this.max_traffic = 100;
                this.error_threshold = 2;
            }
        }

        public CanaryStrategy() {
            this.plugin_name = "canary";
            this.config = new Config();
        }
    }

    public static class PrometheusMonitor {
        String plugin_name;
        Config config;

        public PrometheusMonitor(String prometheusAddress) {
            this.plugin_name = "prometheus";
            this.config = new Config(prometheusAddress);
        }

        static class Config {
            String address;
            List<Query> queries;

            static class Query {
                String name;
                String preset;
                Integer min;
                Integer max;

                Query(String name, String preset, Integer min) {
                    this.name = name;
                    this.preset = preset;
                    this.min = min;
                    this.max = null;
                }

                Query(String name, String preset, Integer min, Integer max) {
                    this.name = name;
                    this.preset = preset;
                    this.min = min;
                    this.max = max;
                }
            }

            public Config(String address) {
                this.address = address;
                this.queries = Lists.newArrayList(
                        new Query("request-success", "envoy-request-success", 99),
                        new Query("request-duration", "envoy-request-duration", 2, 200)
                );
            }
        }
    }

    public Release build(String name, NomadConfig nomadConfig, String prometheusAddress) {
        this.name = name;
        this.releaser = new ConsulReleaser(name);
        this.runtime = new NomadRuntime(nomadConfig);
        this.strategy = new CanaryStrategy();
        this.monitor = new PrometheusMonitor(prometheusAddress);
        return this;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
