network "dev" {
  subnet = "10.6.0.0/16"
}

module "boundary" {
  source = "./boundary"
}

module "monitoring" {
  source = "./monitoring"
}

module "consul_nomad" {
  source = "github.com/shipyard-run/blueprints//modules/consul-nomad"
}

module "vault" {
  source = "./vault"
}

module "releaser" {
  source = "./releaser"
  depends_on = ["module.consul_nomad", "module.monitoring"]
}

module "minecraft" {
  source = "./minecraft"
}

module "browserless" {
  source = "./browserless"
}

module "boundary_connect" {
  source = "./boundary-connect"
  depends_on = ["module.boundary"]
}

module "grafana_alerts" {
  source = "./alerts"
  depends_on = ["module.monitoring"]
}

module "app" {
  source = "./app"
  depends_on = ["module.consul_nomad", "module.monitoring"]
}