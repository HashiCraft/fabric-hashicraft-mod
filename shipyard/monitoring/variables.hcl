variable "network" {
  default = "dev"
}

variable "grafana_version" {
  default = "latest"
}

variable "prometheus_version" {
  default = "2.30.2"
}

variable "loki_version" {
  default = "latest"
}

variable "consul_url" {
  default = "http://1.consul.server.container.shipyard.run:8500"
}