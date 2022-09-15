variable "network" {
  default = "dev"
}

variable "consul_release_controller_tls_cert" {
  default = ""
}

variable "consul_release_controller_tls_key" {
  default = ""
}

variable "consul_release_controller_image" {
  default = "nicholasjackson/consul-release-controller:0.2.4"
}