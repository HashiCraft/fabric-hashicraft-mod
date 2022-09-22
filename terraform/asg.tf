data "aws_ami" "hashicorp" {
  filter {
    name   = "name"
    values = ["hc-base*"]
  }
  most_recent = true
  owners      = ["888995627335"]
}

data "aws_iam_policy" "security_compute_access" {
  name = "SecurityComputeAccess"
}

data "aws_iam_policy_document" "allow_ec2" {
  statement {
    sid     = "AllowEC2"
    effect  = "Allow"
    actions = ["sts:AssumeRole"]
    principals {
      type        = "Service"
      identifiers = ["ec2.amazonaws.com"]
    }
  }
}

resource "aws_iam_role" "server" {
  name                = var.name
  assume_role_policy  = data.aws_iam_policy_document.allow_ec2.json
  managed_policy_arns = [data.aws_iam_policy.security_compute_access.arn]
}

resource "aws_iam_instance_profile" "server" {
  name_prefix = "${var.name}-instance-profile-"
  role        = aws_iam_role.server.name
}

resource "aws_launch_template" "server" {
  name_prefix            = "${var.name}-server-"
  image_id               = data.aws_ami.hashicorp.image_id
  instance_type          = "m5.4xlarge"
  vpc_security_group_ids = [aws_security_group.server.id]

  iam_instance_profile {
    arn = aws_iam_instance_profile.server.arn
  }

  block_device_mappings {
    device_name = "/dev/sdf"

    ebs {
      volume_size = 32
    }
  }

  tag_specifications {
    resource_type = "instance"

    tags = { "Name" = "${var.name}-server" }
  }

  tag_specifications {
    resource_type = "volume"

    tags = { "Name" = "${var.name}-server-volume" }
  }

  tags = { "Name" = "${var.name}-server-launch-template" }

  user_data = base64encode(file("${path.module}/scripts/server.sh"))
}

resource "aws_autoscaling_group" "server" {
  name_prefix = "${var.name}-server-"

  launch_template {
    id      = aws_launch_template.server.id
    version = aws_launch_template.server.latest_version
  }

  load_balancers = [aws_elb.server.name]

  desired_capacity = 1
  min_size         = 1
  max_size         = 1

  # Subnets to launch resources
  vpc_zone_identifier = module.vpc.private_subnets

  instance_refresh {
    strategy = "Rolling"
    preferences {
      min_healthy_percentage = 0
    }
  }
}
