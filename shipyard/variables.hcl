variable "network" {
  default = "dev"
}

// Nomad & Consul
variable "cn_network" {
  default = var.network
}

variable "cn_nomad_cluster_name" {
  default = "nomad_cluster.local"
}

variable "cn_nomad_client_nodes" {
  default = 3
}

variable "cn_consul_version" {
  default = "1.12.2"
}

variable "cn_nomad_version" {
  default = "1.3.1"
}

variable "cn_nomad_client_config" {
  default = "${data("nomad_config")}/client.hcl"
}

// Minecraft
variable "minecraft_mods_path" {
  default = "${file_dir()}/minecraft/mods"
}

variable "minecraft_world_path" {
  default = "${file_dir()}/minecraft/world"
}

variable "minecraft_config_path" {
  default = "${file_dir()}/minecraft/config"
}

variable "minecraft_server_icon_path" {
  default = "${file_dir()}/minecraft"
}

variable "minecraft_world_backup" {
  // World archive to restore to server, only restores when ./minecraft/world folder is empty
  default = "https://github.com/hashicorp-dev-advocates/demo-blueprint/releases/download/v0.2.0/hashiconf.tar.gz"
}

variable "minecraft_mods_backup" {
  // Mods archive to restore to server, only restores when ./minecraft/mods folder is empty
  default = "https://github.com/hashicorp-dev-advocates/demo-blueprint/releases/download/v0.2.0/mods.tar.gz"
}

// Restic
variable "minecraft_enable_backups" {
  default = false
}

variable "minecraft_restic_version" {
  default = "v0.0.1"
}

variable "minecraft_restic_backup_path" {
  default = "${file_dir()}/minecraft/"
}

variable "minecraft_restic_repository" {
  default = ""
}

variable "minecraft_restic_password" {
  default = ""
}

variable "minecraft_restic_key" {
  default = ""
}

variable "minecraft_restic_secret" {
  default = ""
}

// Browserless
variable "browserless_address" {
  default = "http://localhost:28080"
}