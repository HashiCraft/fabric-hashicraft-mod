// template "prometheus_rules" {
//   source = <<-EOF
//   EOF
//   destination = "${data("prometheus")}/rules.yml"
// }

template "prometheus_config" {
  source = <<-EOF
  ---
  global:
    scrape_interval: 30s
    evaluation_interval: 3s

  rule_files:
  - rules.yml

  alerting:
    alertmanagers:
    - static_configs:
      - targets:
        - alertmanager.container.shipyard.run

  scrape_configs:
  - job_name: prometheus
    static_configs:
    - targets:
      - 0.0.0.0:9090
  EOF
  destination = "${data("prometheus")}/prometheus.yml"
}

container "prometheus" {
  image {
    name = "prom/prometheus:v${var.prometheus_version}"
  }

  port {
    local  = 9090
    remote = 9090
  }

  // volume {
  //   source      = "${data("prometheus")}/rules.yml"
  //   destination = "/etc/prometheus/rules.yml"
  // }

  volume {
    source      = "${data("prometheus")}/prometheus.yml"
    destination = "/etc/prometheus/prometheus.yml"
  }

  network {
    name = "network.${var.network}"
  }

  depends_on = [
    "template.prometheus_config",
    // "template.prometheus_rules"
  ]
}