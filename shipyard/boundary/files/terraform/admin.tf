locals {
  admin_username = "admin"
  admin_password = random_password.admin.result
}

resource "boundary_account" "boundary_admin" {
  auth_method_id = boundary_auth_method.password.id
  type           = "password"
  login_name     = local.admin_username
  password       = local.admin_password
}

resource "boundary_user" "boundary_admin" {
  name        = local.admin_username
  description = "Admin user"
  account_ids = [boundary_account.boundary_admin.id]
  scope_id    = boundary_scope.org.id
}

resource "random_password" "admin" {
  length      = 16
  special     = false
  min_numeric = 1
  min_lower   = 1
  min_upper   = 1
}