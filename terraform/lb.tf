resource "aws_elb" "server" {
  name            = "${var.name}-lb"
  subnets         = module.vpc.public_subnets
  security_groups = [aws_security_group.lb.id]

  listener {
    instance_port     = 25565
    instance_protocol = "tcp"
    lb_port           = 25565
    lb_protocol       = "tcp"
  }

  // This might not work for UDP(?)
  listener {
    instance_port     = 19132
    instance_protocol = "tcp"
    lb_port           = 19132
    lb_protocol       = "tcp"
  }

  // grafana
  listener {
    instance_port     = 3000
    instance_protocol = "tcp"
    lb_port           = 3000
    lb_protocol       = "tcp"
  }

  health_check {
    healthy_threshold   = 2
    unhealthy_threshold = 2
    timeout             = 3
    target              = "TCP:25565"
    interval            = 30
  }

  cross_zone_load_balancing = true

  tags = { "Name" = "${var.name}-lb" }
}
