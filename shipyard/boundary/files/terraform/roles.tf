resource "boundary_role" "boundary_org_admin" {
  name           = "hashicorp-admin"
  description    = "admin role for organization"
  scope_id       = boundary_scope.org.id
  grant_scope_id = boundary_scope.org.id

  grant_strings = [
    "id=*;type=*;actions=*"
  ]

  principal_ids = [
    boundary_user.boundary_admin.id
  ]
}

resource "boundary_role" "readonly" {
  name        = "hashicorp-readonly"
  description = "read-only role for organization"

  grant_strings = [
    "id=*;type=*;actions=read",
    "id=*;type=target;actions=read,list,authorize-session",
    "id=*;type=session;actions=read,list"
  ]
  scope_id       = boundary_scope.org.id
  grant_scope_id = boundary_scope.org.id

  principal_ids = [
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
    ## Add HashiCraft team to role based on incident response workflow
    # boundary_group.hashicraft.id
  ]
}