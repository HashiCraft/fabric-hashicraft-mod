variable "grafana_datasource_name" {
  type    = string
  default = "Prometheus"
}

variable "grafana_url" {
  type    = string
  default = "admin:admin@grafana.container.shipyard.run:3000"
}

variable "grafana_folder_name" {
  type    = string
  default = "hashicraft"
}

variable "grafana_organization" {
  type    = number
  default = 1
}

variable "grafana_rule_group" {
  type    = string
  default = "hashicraft"
}

variable "grafana_rule_title" {
  type    = string
  default = "hashicraft"
}

variable "boundary_environment" {
  type    = string
  default = "development"
}

variable "boundary_project" {
  type    = string
  default = "hashiconf"
}

variable "boundary_targets" {
  type    = string
  default = "consul"
}

variable "boundary_teams" {
  type    = string
  default = "hashicraft"
}

variable "rift_url" {
  type    = string
  default = "http://rift.container.shipyard.run:4444/v1/alertmanager"
}
