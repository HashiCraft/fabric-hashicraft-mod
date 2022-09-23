variable "minecraft_version" {
  default = "v1.19.2-fabric"
}

variable "minecraft_mods_path" {
  default = "${data("minecraft")}/mods"
}

variable "minecraft_plugins_path" {
  default = "${data("minecraft")}/plugins"
}

variable "minecraft_world_path" {
  default = "${data("minecraft")}/world"
}

variable "minecraft_world_backup" {
  default = ""
}

variable "minecraft_worlds_path" {
  default = "${data("minecraft")}/worlds"
}

variable "minecraft_config_path" {
  default = "${data("minecraft")}/config"
}

variable "minecraft_server_icon_path" {
  default = "${data("minecraft")}/server-icon.png"
}

variable "minecraft_memory" {
  default = "2G"
}

// Geyser
variable "minecraft_geyser_version" {
  default = "v0.0.2"
}

// Restic
variable "minecraft_restic_repository" {
  default = ""
}

variable "minecraft_restic_key" {
  default = ""
}

variable "minecraft_restic_secret" {
  default = ""
}

variable "minecraft_restic_password" {
  default = ""
}

variable "minecraft_restic_version" {
  default = "v0.0.1"
}

variable "minecraft_restic_backup_path" {
  default = "${file_dir()}/backups/minecraft"
}

variable "minecraft_restic_backup_interval" {
  default = 1200
}

