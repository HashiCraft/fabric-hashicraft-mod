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

resource "boundary_host_catalog" "consul" {
  scope_id = boundary_scope.project.id
  type     = "static"
  name     = "consul"
}

resource "boundary_host" "consul" {
  name            = "consul"
  address         = "1.consul.server.container.shipyard.run"
  host_catalog_id = boundary_host_catalog.consul.id
  type            = "static"
}

resource "boundary_host_set" "consul" {
  host_catalog_id = boundary_host_catalog.consul.id
  type            = "static"

  host_ids = [
    boundary_host.consul.id
  ]
}

resource "boundary_target" "consul" {
  name     = "consul"
  scope_id = boundary_scope.project.id
  type     = "tcp"

  host_source_ids = [
    boundary_host_set.consul.id
  ]

  default_port             = 8500
  session_connection_limit = 5
  session_max_seconds      = 30
}