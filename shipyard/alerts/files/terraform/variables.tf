variable "grafana_datasource_name" {
  type = string
  default = "Prometheus"
}

variable "grafana_url" {
  type = string
  default = "admin:admin@grafana.ingress.shipyard.run"
}