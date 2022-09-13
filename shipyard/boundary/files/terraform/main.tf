resource "boundary_scope" "org" {
  scope_id    = "global"
  name        = "hashicorp"
  description = "HashiCorp org"

  auto_create_admin_role   = false
  auto_create_default_role = false
}

resource "boundary_scope" "project" {
  name                     = "hashiconf"
  description              = "HashiConf project"
  scope_id                 = boundary_scope.org.id
  auto_create_admin_role   = false
  auto_create_default_role = false
}

resource "boundary_auth_method" "password" {
  scope_id = boundary_scope.org.id
  type     = "password"
}

resource "boundary_role" "global_anon_listing" {
  scope_id = "global"

  grant_strings = [
    "id=*;type=auth-method;actions=list,authenticate",
    "id=*;type=scope;actions=list,read",
    "id={{account.id}};actions=read,change-password"
  ]

  principal_ids = [
    "u_anon"
  ]
}

resource "boundary_role" "org_anon_listing" {
  scope_id = boundary_scope.org.id

  grant_strings = [
    "id=*;type=auth-method;actions=list,authenticate",
    "type=scope;actions=list",
    "id={{account.id}};actions=read,change-password"
  ]

  principal_ids = [
    "u_anon"
  ]
}

resource "boundary_host_catalog" "postgres" {
  scope_id = boundary_scope.project.id
  type     = "static"
  name     = "postgres"
}

resource "boundary_host" "postgres" {
  name            = "postgres"
  address         = "postgres.container.shipyard.run"
  host_catalog_id = boundary_host_catalog.postgres.id
  type            = "static"
}

resource "boundary_host_set" "postgres" {
  host_catalog_id = boundary_host_catalog.postgres.id
  type            = "static"

  host_ids = [
    boundary_host.postgres.id
  ]
}

resource "boundary_target" "postgres" {
  name     = "postgres"
  scope_id = boundary_scope.project.id
  type     = "tcp"

  host_source_ids = [
    boundary_host_set.postgres.id
  ]

  default_port = 5432
}

resource "boundary_account" "boundary_admin" {
  auth_method_id = boundary_auth_method.password.id
  type           = "password"
  login_name     = "admin"
  password       = "password"
}

resource "boundary_user" "boundary_admin" {
  name        = "admin"
  description = "Admin user"
  account_ids = [boundary_account.boundary_admin.id]
  scope_id    = boundary_scope.org.id
}

resource "boundary_role" "boundary_org_admin" {
  name           = "HashiCorp admin"
  scope_id       = boundary_scope.org.id
  grant_scope_id = boundary_scope.org.id

  grant_strings = [
    "id=*;type=*;actions=*"
  ]

  principal_ids = [boundary_user.boundary_admin.id]
}

resource "boundary_role" "boundary_project_admin" {
  name           = "HashiConf admin"
  scope_id       = boundary_scope.project.id
  grant_scope_id = boundary_scope.project.id

  grant_strings = [
    "id=*;type=*;actions=*"
  ]

  principal_ids = [boundary_user.boundary_admin.id]
}
