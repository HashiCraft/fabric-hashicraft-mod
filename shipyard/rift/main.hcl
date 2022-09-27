template "rift_config" {
  source = <<EOT
  #!/bin/bash
  ORGANIZATION=$(cat /boundary/organization)
  SCOPE=$(cat /boundary/scope)
  AUTH_METHOD=$(cat /boundary/auth_method_id)
  USERNAME=$(cat /boundary/username)
  PASSWORD=$(cat /boundary/password)

  cat <<EOF > /rift/config.json
{
  "log": {
    "level": "debug"
  },
  "alertmanager": {
    "enabled": true
  },
  "boundary": {
    "organization": "$${ORGANIZATION}",
    "scope": "$${SCOPE}",
    "auth": {
      "method": "$${AUTH_METHOD}",
      "username": "$${USERNAME}",
      "password": "$${PASSWORD}"
    }
  }
}
EOF
  EOT
  destination = "${data("rift")}/generate.sh"
}

exec_remote "rift_config" {
  depends_on = ["template.rift_config"]
  
  image {
    name = "shipyardrun/hashicorp-tools:v0.10.0"
  }

  cmd = "/bin/bash"
  args = [
    "/rift/generate.sh"
  ]

  volume {
    source = data("boundary-auth")
    destination = "/boundary"
  }

  volume {
    source = "${data("rift")}"
    destination = "/rift"
  }
}

container "rift" {
  depends_on = ["exec_remote.rift_config"]

  image {
    name = "hashicraft/rift:${var.rift_version}"
  }

  port {
    local  = 4444
    remote = 4444
  }

  env {
    key = "LOG_LEVEL"
    value = "debug"
  }

  env {
    key = "ALERT_MANAGER_ENABLED"
    value = true
  }

  env {
    key = "BOUNDARY_ADDR"
    value = "http://boundary.container.shipyard.run:9200"
  }

  volume {
    source = "${data("rift")}"
    destination = "/config"
  }

  network {
    name = "network.${var.network}"
  }
}