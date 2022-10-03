output "server_lb_dns" {
  value       = aws_elb.server.dns_name
  description = "HashiCraft load balancer DNS name"
}

output "server_nlb_dns" {
  value       = module.nlb.lb_dns_name
  description = "HashiCraft NLB load balancer DNS name"
}
