output "CONSUL_RELEASER_ADDR" {
  value = "http://releaser.container.shipyard.run:8080"
}

output "BOUNDARY_ADDR" {
  value = "http://boundary.container.shipyard.run:9200"
}

output "BOUNDARY_PROXY_ADDR" {
  value = "http://boundary-proxy.container.shipyard.run:38500"
}