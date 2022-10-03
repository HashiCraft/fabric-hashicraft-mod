module "nlb" {
  source  = "terraform-aws-modules/alb/aws"
  version = "~> 6.0"

  name = "${var.name}-nlb"

  load_balancer_type = "network"

  vpc_id  = module.vpc.vpc_id
  subnets = module.vpc.public_subnets

  target_groups = [
    {
      name_prefix      = "g-"
      backend_protocol = "UDP"
      backend_port     = 19132
      target_type      = "instance"
      health_check = {
        protocol = "TCP"
        port     = 25565
      }
    },
    {
      name_prefix      = "mc-"
      backend_protocol = "TCP"
      backend_port     = 25565
      target_type      = "instance"
    }
  ]

  http_tcp_listeners = [
    {
      port               = 19132
      protocol           = "UDP"
      target_group_index = 0
    },
    {
      port               = 25565
      protocol           = "TCP"
      target_group_index = 1
    }
  ]

  tags = { "Name" = "${var.name}-nlb" }
}