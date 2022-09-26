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
    boundary_user.boundary_admin.id,
    boundary_group.hashicraft.id
  ]
}