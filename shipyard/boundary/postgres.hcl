container "postgres" {
  image {
    name = "postgres:${var.boundary_postgres_version}"
  }

  port {
    local  = 5432
    host   = 5432
    remote = 5432
  }

  env {
    key   = "POSTGRES_USER"
    value = var.boundary_postgres_user
  }

  env {
    key   = "POSTGRES_PASSWORD"
    value = var.boundary_postgres_password
  }

  volume {
    source      = "files/postgres"
    destination = "/files"
  }

  network {
    name = "network.${var.network}"
  }
}

exec_remote "postgres_readiness_check" {
  target = "container.postgres"

  cmd = "sh"
  args = [
    "/files/check.sh"

  ]

  network {
    name = "network.${var.network}"
  }

  depends_on = [
    "container.postgres"
  ]
}