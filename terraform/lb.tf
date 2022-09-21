resource "aws_elb" "server" {
  name            = "${var.name}-lb"
  subnets         = module.vpc.public_subnets
  security_groups = [aws_security_group.lb.id]

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
    target              = "HTTP:3000/"
    interval            = 30
  }

  cross_zone_load_balancing = true

  tags = { "Name" = "${var.name}-lb" }
}