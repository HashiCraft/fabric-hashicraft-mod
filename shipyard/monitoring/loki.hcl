// template "loki_config" {
//   source = <<-EOF
//   EOF
//   destination = "${data("loki")}/loki.yml"
// }

container "loki" {
  image {
    name = "grafana/loki:${var.loki_version}"
  }

  port {
    local  = 3100
    remote = 3100
  }

  port {
    local  = 9095
    remote = 9095
  }

  // volume {
  //   source      = "${data("loki")}/loki.yml"
  //   destination = "/etc/loki/config/loki.yml"
  // }

  network {
    name = "network.${var.network}"
  }

  depends_on = [
    // "template.loki_config",
  ]
}