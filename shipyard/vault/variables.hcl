variable "vault_bootstrap_script" {
  default = <<-EOF
  #/bin/sh -e
  vault status
  vault secrets enable kv-v2
  vault auth enable userpass

  vault kv put secret/minecraft/level-1 access=true
  vault kv put secret/minecraft/level-2 access=true
  vault kv put secret/minecraft/incident-response access=true
  
  vault policy write level-1 /data/policies/level-1.hcl
  vault policy write level-2 /data/policies/level-2.hcl
  vault policy write incident-response /data/policies/incident-response.hcl
  EOF
}

variable "vault_network" {
  default = var.network
}

variable "vault_version" {
  default = "1.11.3"
}