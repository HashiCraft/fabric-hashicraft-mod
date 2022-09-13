variable "network" {
  default = "dev"
}

variable "boundary_version" {
  default = "0.10.0"
}

variable "boundary_postgres_version" {
  default = "14.5"
}

variable "boundary_postgres_user" {
  default = "postgres"
}

variable "boundary_postgres_password" {
  default = "postgres"
}

variable "boundary_address" {
  default = "http://boundary.container.shipyard.run:9200"
}