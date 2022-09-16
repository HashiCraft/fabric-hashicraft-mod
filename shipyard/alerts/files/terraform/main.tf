data "terracurl_request" "datasource" {
  name           = "datasource"
  url            = "http://${var.grafana_url}/api/datasources/name/${var.grafana_datasource_name}"
  method         = "GET"
  headers = {
    Content-Type = "application/json"
  }
}

output "datasource_id" {
  value = data.terracurl_request.datasource.response
}

# resource "terracurl_request" "mount" {
#   name           = "vault-mount"
#   url            = "http://localhost:8200/v1/sys/mounts/aws"
#   method         = "POST"
#   request_body   = <<-EOF
#   {
#     "type": "aws",
#     "config": {
#       "force_no_cache": true
#     }
#   }
#   EOF

#   headers = {
#     X-Vault-Token = "root"
#   }

#   response_codes = [200,204]

#   destroy_url    = "http://localhost:8200/v1/sys/mounts/aws"
#   destroy_method = "DELETE"

#   destroy_headers = {
#     X-Vault-Token = "root"
#   }

#   destroy_response_codes = [204]
# }

# resource "shell_script" "grafana_alerts" {
#   environment = {
#     GRAFANA_URL = "admin:admin@grafana.ingress.shipyard.run"

#     DATASOURCE_NAME = "Prometheus"
#     ORGANIZATION    = "1"
#     FOLDER_NAME     = var.application_name
#     GROUP           = var.application_name
#     TITLE           = var.application_name

#     ENVIRONMENT = "production"
#     PROJECT     = boundary_scope.project.id
#     TARGETS     = boundary_target.application_database.id
#     TEAMS       = join(",", var.boundary_groups)
#   }

#   sensitive_environment = {}

#   lifecycle_commands {
#     create = file("${path.module}/files/create.sh")
#     delete = file("${path.module}/files/delete.sh")
#   }

#   interpreter = ["/bin/bash", "-c"]

#   working_directory = path.module
# }