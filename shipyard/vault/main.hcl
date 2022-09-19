copy "vault_policies" {
  source      = "${file_dir()}/files/vault"
  destination = "${data("vault_data")}/policies"
}

module "vault" {
  depends_on = ["copy.vault_policies"]
  source     = "github.com/shipyard-run/blueprints//modules/vault-dev"
}