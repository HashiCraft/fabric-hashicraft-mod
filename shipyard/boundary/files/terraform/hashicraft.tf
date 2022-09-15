locals {
  hashicraft_username = "hashicraft"
  hashicraft_password = random_password.hashicraft.result
}

resource "random_password" "hashicraft" {
  length      = 16
  special     = false
  min_numeric = 1
  min_lower   = 1
  min_upper   = 1
}

resource "boundary_account" "hashicraft" {
  auth_method_id = boundary_auth_method.password.id
  type           = "password"
  login_name     = local.hashicraft_username
  password       = local.hashicraft_password
}

resource "boundary_user" "hashicraft" {
  name        = local.hashicraft_username
  description = "${local.hashicraft_username} user for incident response workflow"
  account_ids = [boundary_account.hashicraft.id]
  scope_id    = boundary_scope.org.id
}

resource "boundary_group" "hashicraft" {
  name        = "hashicraft"
  description = "group for incident response workflow"
  member_ids  = [boundary_user.hashicraft.id]
  scope_id    = boundary_scope.project.id
}