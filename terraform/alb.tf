module "nlb" {
  source  = "terraform-aws-modules/alb/aws"
  version = "~> 6.0"

  name = "${var.name}-nlb"

  load_balancer_type = "network"

  vpc_id             = module.vpc.vpc_id
  subnets            = module.vpc.public_subnets
  security_groups    = [aws_security_group.lb.id]

   target_groups = [
    {
      name_prefix      = "tg-"
      backend_protocol = "TCP_UDP"
      backend_port     = 19132
      target_type      = "instance"
    }
  ]

  http_tcp_listeners = [
    {
      port               = 19132
      protocol           = "TCP_UDP"
      target_group_index = 0
    }
  ]

  tags = { "Name" = "${var.name}-nlb" }
}