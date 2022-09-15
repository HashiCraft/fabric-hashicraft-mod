certificate_ca "releaser_ca" {
  output = data("consul-release-controller")
}

certificate_leaf "releaser_leaf" {
  depends_on = ["certificate_ca.releaser_ca"]

  ca_cert = "${data("consul-release-controller")}/releaser_ca.cert"
  ca_key  = "${data("consul-release-controller")}/releaser_ca.key"

  ip_addresses = ["127.0.0.1"]
  dns_names    = ["127.0.0.1:9443"]

  output = data("consul-release-controller")
}

container "releaser" {
  image {
    name = var.consul_release_controller_image
  }

  port {
    local  = 8080
    remote = 8080
    host   = 8080
  }

  env {
    key   = "ENABLE_NOMAD"
    value = "true"
  }

  env {
    key   = "TLS_CERT"
    value = "/certs/releaser_leaf.cert"
  }

  env {
    key   = "TLS_KEY"
    value = "/certs/releaser_leaf.key"
  }

  env {
    key   = "CONSUL_HTTP_ADDR"
    value = "http://1.consul.server.container.shipyard.run:8500"
  }

  env {
    key   = "NOMAD_ADDR"
    value = "http://server.local.nomad-cluster.shipyard.run:4646"
  }

  env {
    key   = "HTTP_API_BIND_ADDRESS"
    value = "0.0.0.0"
  }

  network {
    name = "network.${var.network}"
  }

  volume {
    source      = data("consul-release-controller")
    destination = "/certs"
  }

  resources {
    cpu    = 500
    memory = 128
  }

  depends_on = [
    "certificate_leaf.releaser_leaf"
  ]
}