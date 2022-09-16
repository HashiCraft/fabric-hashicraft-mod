template "alerts_setup" {
  source = <<EOT
  #!/bin/bash
  terraform -chdir=/terraform init
  terraform -chdir=/terraform apply -auto-approve
  EOT
  destination = "${data("alerts")}/setup.sh"
}

exec_remote "alerts_setup" {
  image {
    name = "shipyardrun/hashicorp-tools:v0.10.0"
  }

  cmd = "/bin/bash"
  args = [
    "/setup.sh"
  ]

  volume {
    source = "${data("alerts")}/setup.sh"
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
    "template.alerts_setup",
    "container.grafana"
  ]
}