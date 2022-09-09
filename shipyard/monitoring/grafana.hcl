template "grafana_prometheus_datasource" {
  source = <<-EOF
  apiVersion: 1
  deleteDatasources:
  - name: Prometheus
    orgId: 1
  datasources:
  - name: Prometheus
    type: prometheus
    access: proxy
    orgId: 1
    url: http://prometheus.container.shipyard.run:9090
    version: 1
    editable: true
  EOF
  destination = "${data("grafana_datasources")}/prometheus.yml"
}

template "grafana_loki_datasource" {
  source = <<-EOF
  apiVersion: 1
  deleteDatasources:
  - name: Loki
    orgId: 1
  datasources:
  - name: Loki
    type: loki
    access: proxy
    orgId: 1
    url: http://loki.container.shipyard.run:3100
    version: 1
    editable: true
  EOF
  destination = "${data("grafana_datasources")}/loki.yml"
}

template "grafana_dashboards" {
  source = <<-EOF
  apiVersion: 1
  providers:
  - name: dashboards
    type: file
    updateIntervalSeconds: 30
    options:
      path: /etc/dashboards
  EOF
  destination = "${data("grafana")}/main.yml"
}

template "grafana_config" {
  source = <<-EOF
  [auth.anonymous]
  enabled = true

  # Organization name that should be used for unauthenticated users
  org_name = Main Org.

  # Role for unauthenticated users, other valid values are `Editor` and `Admin`
  org_role = Viewer

  # Hide the Grafana version text from the footer and help tooltip for unauthenticated users (default: false)
  hide_version = true
  EOF
  destination = "${data("grafana")}/grafana.ini"
}

container "grafana" {
  image {
    name = "grafana/grafana:${var.grafana_version}"
  }

  port {
    local  = 3000
    host   = 3000
    remote = 3000
  }

  volume {
    source      = "${data("grafana")}/grafana.ini"
    destination = "/etc/grafana/grafana.ini"
  }

  volume {
    source      = "${data("grafana")}/main.yml"
    destination = "/etc/grafana/provisioning/dashboards/main.yml"
  }

  volume {
    source      = "${data("grafana_datasources")}"
    destination = "/etc/grafana/provisioning/datasources"
  }

  volume {
    source      = "${file_dir()}/files/grafana/dashboards"
    destination = "/etc/dashboards"
  }

  network {
    name = "network.${var.network}"
  }

  depends_on = [
    "template.grafana_config",
    "template.grafana_dashboards",
    "template.grafana_loki_datasource",
    "template.grafana_prometheus_datasource"
  ]
}