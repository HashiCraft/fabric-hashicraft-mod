output "auth_method_id" {
  value       = boundary_auth_method.password.id
  description = "Auth method ID for Boundary authentication"
}

output "username" {
  value       = boundary_account.boundary_admin.login_name
  description = "Username for Boundary authentication"
}

output "password" {
  value       = boundary_account.boundary_admin.password
  description = "Password for Boundary authentication"
  sensitive   = true
}

output "target_id" {
  value = boundary_target.postgres.id
  description = "Target ID for Boundary session"
}