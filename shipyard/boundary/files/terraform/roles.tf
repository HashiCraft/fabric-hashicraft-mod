resource "boundary_role" "boundary_org_admin" {
  name           = "hashicorp-admin"
  description    = "admin role for organization"
  scope_id       = boundary_scope.org.id
  grant_scope_id = boundary_scope.org.id

  grant_strings = [
    "id=*;type=*;actions=*"
  ]

  principal_ids = [
    boundary_user.boundary_admin.id,
    boundary_group.hashicraft.id
  ]
}

resource "boundary_role" "boundary_project_admin" {
  name           = "hashiconf-admin"
  scope_id       = boundary_scope.project.id
  grant_scope_id = boundary_scope.project.id

  grant_strings = [
    "id=*;type=*;actions=*"
  ]

  principal_ids = [
    boundary_user.boundary_admin.id
  ]
}

resource "boundary_role" "boundary_project_create_role" {
  name           = "hashiconf-incident-role"
  scope_id       = boundary_scope.project.id
  grant_scope_id = boundary_scope.project.id

  grant_strings = [
    "id=*;type=role;actions=*",
    "id=*;type=*;actions=read,list",
    "id=*;type=target;actions=read,list",
    "id=*;type=session;actions=*"
  ]

  principal_ids = [
    boundary_group.hashicraft.id
  ]
}