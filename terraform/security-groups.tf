resource "aws_security_group" "server" {
  name_prefix = "${var.name}-server"
  description = "Security Group for the server instance"
  vpc_id      = module.vpc.vpc_id
  tags        = { "Name" = "${var.name}-server" }
}

resource "aws_security_group_rule" "allow_server_traffic" {
  security_group_id        = aws_security_group.server.id
  source_security_group_id = aws_security_group.server.id
  type                     = "ingress"
  protocol                 = "all"
  from_port                = 0
  to_port                  = 0
  description              = "Allow all traffic within server security group"
}

resource "aws_security_group_rule" "server_allow_outbound" {
  security_group_id = aws_security_group.server.id
  type              = "egress"
  protocol          = "-1"
  from_port         = 0
  to_port           = 0
  cidr_blocks       = ["0.0.0.0/0"]
  ipv6_cidr_blocks  = ["::/0"]
  description       = "Allow any outbound traffic"
}

resource "aws_security_group_rule" "server_allow_minecraft_from_lb" {
  security_group_id        = aws_security_group.server.id
  source_security_group_id = aws_security_group.lb.id
  type                     = "ingress"
  protocol                 = "tcp"
  from_port                = 25565
  to_port                  = 25565
  description              = "Allow traffic to Minecraft from lb to server security group"
}

resource "aws_security_group_rule" "server_allow_geyser_from_internet" {
  security_group_id        = aws_security_group.server.id
  cidr_blocks       = ["0.0.0.0/0"]
  type                     = "ingress"
  protocol                 = "udp"
  from_port                = 19132
  to_port                  = 19132
  description              = "Allow traffic to Geyser from lb to server security group"
}

resource "aws_security_group_rule" "server_allow_minecraft_from_internet" {
  security_group_id        = aws_security_group.server.id
  cidr_blocks       = ["0.0.0.0/0"]
  type                     = "ingress"
  protocol                 = "udp"
  from_port                = 25565
  to_port                  = 25565
  description              = "Allow traffic to Minecraft from lb to server security group"
}

resource "aws_security_group_rule" "server_allow_geyser_from_lb" {
  security_group_id        = aws_security_group.server.id
  source_security_group_id = aws_security_group.lb.id
  type                     = "ingress"
  protocol                 = "udp"
  from_port                = 19132
  to_port                  = 19132
  description              = "Allow traffic to Geyser from lb to server security group"
}

resource "aws_security_group_rule" "server_allow_grafana_from_lb" {
  security_group_id        = aws_security_group.server.id
  source_security_group_id = aws_security_group.lb.id
  type                     = "ingress"
  protocol                 = "tcp"
  from_port                = 3000
  to_port                  = 3000
  description              = "Allow traffic to Grafana from lb to server security group"
}

resource "aws_security_group_rule" "server_allow_consul_from_lb" {
  security_group_id        = aws_security_group.server.id
  source_security_group_id = aws_security_group.lb.id
  type                     = "ingress"
  protocol                 = "tcp"
  from_port                = 8500
  to_port                  = 8500
  description              = "Allow traffic to Consul from lb to server security group"
}

resource "aws_security_group_rule" "server_allow_boundary_from_lb" {
  security_group_id        = aws_security_group.server.id
  source_security_group_id = aws_security_group.lb.id
  type                     = "ingress"
  protocol                 = "tcp"
  from_port                = 9200
  to_port                  = 9200
  description              = "Allow traffic to Boundary from lb to server security group"
}

resource "aws_security_group" "lb" {
  name_prefix = "${var.name}-lb"
  description = "Security groups for the load balancer in front of server"
  vpc_id      = module.vpc.vpc_id
  tags        = { "Name" = "${var.name}-lb" }
}

resource "aws_security_group_rule" "lb_allow_minecraft_inbound" {
  security_group_id = aws_security_group.lb.id
  type              = "ingress"
  protocol          = "tcp"
  from_port         = 25565
  to_port           = 25565
  cidr_blocks       = ["0.0.0.0/0"]
  description       = "Allow inbound traffic to Minecraft"
}

resource "aws_security_group_rule" "lb_allow_geyser_inbound" {
  security_group_id = aws_security_group.lb.id
  type              = "ingress"
  protocol          = "udp"
  from_port         = 19132
  to_port           = 19132
  cidr_blocks       = ["0.0.0.0/0"]
  description       = "Allow inbound traffic to Minecraft"
}

resource "aws_security_group_rule" "lb_allow_grafana_inbound" {
  security_group_id = aws_security_group.lb.id
  type              = "ingress"
  protocol          = "tcp"
  from_port         = 3000
  to_port           = 3000
  cidr_blocks       = ["0.0.0.0/0"]
  description       = "Allow inbound traffic to Grafana"
}

resource "aws_security_group_rule" "lb_allow_consul_inbound" {
  security_group_id = aws_security_group.lb.id
  type              = "ingress"
  protocol          = "tcp"
  from_port         = 8500
  to_port           = 8500
  cidr_blocks       = ["0.0.0.0/0"]
  description       = "Allow inbound traffic to Consul"
}

resource "aws_security_group_rule" "lb_allow_boundary_inbound" {
  security_group_id = aws_security_group.lb.id
  type              = "ingress"
  protocol          = "tcp"
  from_port         = 9200
  to_port           = 9200
  cidr_blocks       = ["0.0.0.0/0"]
  description       = "Allow inbound traffic to Boundary"
}

resource "aws_security_group_rule" "lb_allow_outbound" {
  security_group_id = aws_security_group.lb.id
  type              = "egress"
  protocol          = "-1"
  from_port         = 0
  to_port           = 0
  cidr_blocks       = ["0.0.0.0/0"]
  ipv6_cidr_blocks  = ["::/0"]
  description       = "Allow any outbound traffic"
}
