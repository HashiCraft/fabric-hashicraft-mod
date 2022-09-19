template "set_defaults" {
  source = <<EOT
  #!/bin/sh
  until curl -s $CONSUL_HTTP_ADDR/v1/status/leader | grep 8300; do
    echo "Waiting for Consul to start"
    sleep 1
  done

  consul config write ./proxy-defaults.hcl
  consul config write ./service-defaults.hcl
  consul config write ./payments-retry.hcl

  sleep 5
  EOT
  destination = "${data("app")}/set_defaults.sh"
}

exec_remote "proxy_defaults" {
  depends_on = ["template.set_defaults"]

  image {
    name = "consul:${var.consul_version}"
  }

  network {
    name = "network.${var.network}"
  }

  cmd = "/bin/sh"
  args = [
    "/set_defaults.sh"
  ]

  volume {
    source = "${data("app")}/set_defaults.sh"
    destination = "/set_defaults.sh"
  }

  volume {
    source      = "${file_dir()}/files/consul"
    destination = "/config"
  }

  working_directory = "/config"

  env {
    key   = "CONSUL_HTTP_ADDR"
    value = "http://1.consul.server.container.shipyard.run:8500"
  }
}

nomad_job "jobs" {
  depends_on = ["template.set_defaults", "exec_remote.proxy_defaults"]
  cluster = var.cn_nomad_cluster_name
  paths = [
    "${file_dir()}/files/nomad/api.hcl",
    "${file_dir()}/files/nomad/ingress.hcl",
    "${file_dir()}/files/nomad/loadtest.hcl",
  ]
}