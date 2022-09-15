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

module "releaser" {
  source = "./releaser"
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