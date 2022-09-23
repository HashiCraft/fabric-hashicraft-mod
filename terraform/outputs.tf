output "server_lb_dns" {
  value       = aws_elb.server.dns_name
  description = "HashiCraft load balancer DNS name"
}