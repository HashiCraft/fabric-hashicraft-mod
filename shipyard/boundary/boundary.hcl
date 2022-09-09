template "boundary_config" {
  source = file("files/boundary/config.hcl")
  destination = "${data("boundary")}/config.hcl"
}

template "boundary_recovery" {
  source = file("files/boundary/recovery.hcl")
  destination = "${data("boundary")}/recovery.hcl"
}

exec_remote "boundary_init" {
  image  {
    name = "hashicorp/boundary:${var.boundary_version}"
  }

  cmd = "boundary"
  args = [
    "database",
    "init",
    "-skip-target-creation",
    "-skip-scopes-creation",
    "-skip-host-resources-creation",
    "-skip-auth-method-creation",
    "-config=/boundary/config.hcl"
  ]

  env {
    key = "BOUNDARY_POSTGRES_URL"
    value = "postgresql://${var.boundary_postgres_user}:${var.boundary_postgres_password}@postgres.container.shipyard.run:5432/postgres?sslmode=disable"
  }

  network {
      name = "network.${var.network}"
  }

  volume {
    source = data("boundary")
    destination = "/boundary"
  }

  depends_on = [
    "template.boundary_config",
    "template.boundary_recovery",
    "container.postgres",
    "exec_remote.postgres_readiness_check"
  ]
}

container "boundary" {
  image  {
    name = "hashicorp/boundary:${var.boundary_version}"
  }

  command = [
    "boundary",
    "server",
    "-config=/boundary/config.hcl"
  ]

  port {
    local = 9200
    remote = 9200
    host = 9200
  }

  port {
    local = 9201
    remote = 9201
    host = 9201
  }

  port {
    local = 9202
    remote = 9202
    host = 9202
  }

  privileged = true

  env {
    key   = "BOUNDARY_POSTGRES_URL"
    value = "postgresql://${var.boundary_postgres_user}:${var.boundary_postgres_password}@postgres.container.shipyard.run:5432/postgres?sslmode=disable"
  }

  env {
    key   = "BOUNDARY_ADDR"
    value = "http://localhost:9200"
  }

  network {
    name = "network.${var.network}"
  }

  volume {
    source = data("boundary")
    destination = "/boundary"
  }
  
  depends_on = [
    "template.boundary_config",
    "template.boundary_recovery",
    "exec_remote.boundary_init",
    "exec_remote.postgres_readiness_check"
  ]
}

template "boundary_setup" {
  source = <<EOT
  #!/bin/bash
  terraform -chdir=/terraform init
  terraform -chdir=/terraform apply -auto-approve
  EOT
  destination = "${data("boundary")}/setup.sh"
}

exec_remote "boundary_setup" {
  image {
    name = "shipyardrun/hashicorp-tools:v0.9.0"
  }

  cmd = "/bin/bash"
  args = [
    "/setup.sh"
  ]

  volume {
    source = "${data("boundary")}/setup.sh"
    destination = "/setup.sh"
  }

  volume {
    source = "${file_dir()}/files/terraform"
    destination = "/terraform"
  }

  network {
    name = "network.${var.network}"
  }

  depends_on = [
    "template.boundary_setup",
    "container.boundary"
  ]
}