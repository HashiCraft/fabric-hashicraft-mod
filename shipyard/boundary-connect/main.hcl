template "boundary_auth" {
  source      = <<EOT
  #!/bin/sh
  boundary authenticate password \
    -auth-method-id=$(cat /boundary-auth/auth_method_id) \
    -login-name=$(cat /boundary-auth/username) \
    -password=file:///boundary-auth/password | grep -v ':' | grep -v -e '^[[:space:]]*$' > /boundary-auth/token
  EOT
  destination = "${data("boundary-auth")}/setup.sh"
}

exec_remote "boundary_auth" {
  image {
    name = "hashicorp/boundary:${var.boundary_version}"
  }

  cmd  = "/bin/sh"
  args = [
    "/boundary-auth/setup.sh"
  ]

  volume {
    source      = "${data("boundary-auth")}"
    destination = "/boundary-auth"
  }

  network {
    name = "network.${var.network}"
  }

  env {
    key   = "BOUNDARY_ADDR"
    value = var.boundary_address
  }

  env {
    key   = "BOUNDARY_CLI_FORMAT"
    value = "table"
  }

  env {
    key   = "BOUNDARY_KEYRING_TYPE"
    value = "none"
  }
}

template "boundary_proxy" {
  source      = <<EOT
  #!/bin/bash
  while true
  do
    boundary connect -token=file:///boundary-auth/token -target-id=$(cat /boundary-auth/target_id)
    sleep 5
  done
  EOT
  destination = "${data("boundary-proxy")}/setup.sh"
}

container "boundary_proxy" {
  image {
    name = "joatmon08/hashicorp-tools-psql:0.10.0"
  }

  command = ["/bin/sh", "/boundary-proxy/setup.sh"]

  port {
    local  = var.boundary_proxy_listen_port
    remote = var.boundary_proxy_listen_port
    host   = var.boundary_proxy_listen_port
  }

  env {
    key   = "BOUNDARY_ADDR"
    value = var.boundary_address
  }

  env {
    key   = "BOUNDARY_CONNECT_LISTEN_ADDR"
    value = "0.0.0.0"
  }

  env {
    key   = "BOUNDARY_CONNECT_LISTEN_PORT"
    value = "${var.boundary_proxy_listen_port}"
  }

  network {
    name = "network.${var.network}"
  }

  volume {
    source      = data("boundary-auth")
    destination = "/boundary-auth"
  }

  volume {
    source      = data("boundary-proxy")
    destination = "/boundary-proxy"
  }

  depends_on = [
    "template.boundary_auth",
    "exec_remote.boundary_auth",
  ]
}