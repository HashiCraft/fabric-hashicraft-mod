output "auth_method_id" {
  value       = boundary_auth_method.password.id
  description = "Auth method ID for Boundary authentication"
}

output "username" {
  value       = boundary_account.hashicraft.login_name
  description = "Username for Boundary authentication"
}

output "password" {
  value       = boundary_account.hashicraft.password
  description = "Password for Boundary authentication"
  sensitive   = true
}

output "target_id" {
  value       = boundary_target.consul.id
  description = "Target ID for Boundary session"
}

output "admin_password" {
  value       = boundary_account.boundary_admin.password
  description = "Admin password for Boundary authentication"
  sensitive   = true
}

output "organization" {
  value = boundary_scope.org.id
  description = "Organization scope that rift will use to create roles in"
}

output "scope" {
  value = boundary_scope.project.id
  description = "Project scope that rift will use to create roles in"
}