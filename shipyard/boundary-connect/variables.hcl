variable "network" {
  default = "dev"
}

variable "boundary_version" {
  default = "0.10.5"
}

variable "boundary_address" {
  default = "http://boundary.container.shipyard.run:9200"
}

variable "boundary_proxy_listen_port" {
  default = 38500
}