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
  managed_policy_arns = [data.aws_iam_policy.security_compute_access.arn, aws_iam_policy.hashicraft_backup.arn]
}

resource "aws_iam_instance_profile" "server" {
  name_prefix = "${var.name}-instance-profile-"
  role        = aws_iam_role.server.name
}

resource "aws_instance" "server" {
  ami           = data.aws_ami.hashicorp.id
  instance_type = "m5.4xlarge"

  subnet_id       = module.vpc.private_subnets.0
  # security_groups = [aws_security_group.server.id]

  vpc_security_group_ids = [aws_security_group.server.id]

  iam_instance_profile = aws_iam_instance_profile.server.name

  tags = { "Name" = "${var.name}-server" }

  user_data = base64encode(templatefile("${path.module}/scripts/server.sh", {
    minecraft_restic_repository = "s3:s3.amazonaws.com/${aws_s3_bucket.hashicraft_backup.bucket}"
    minecraft_restic_password   = random_password.hashicraft_backup.result
  }))

  root_block_device {
    delete_on_termination = false
    encrypted             = false
    tags = {
      "Name" = "${var.name}-server-volume"
    }
    volume_size = 8
    volume_type = "gp2"
  }

  ebs_block_device {
    delete_on_termination = true
    device_name           = "/dev/sdf"
    encrypted             = false
    tags = {
      "Name" = "${var.name}-server-volume-docker"
    }
    volume_size = 32
    volume_type = "gp2"
  }
}

resource "aws_elb_attachment" "server" {
  elb      = aws_elb.server.id
  instance = aws_instance.server.id
}

resource "aws_lb_target_group_attachment" "geyser" {
  target_group_arn = module.nlb.target_group_arns[0]
  target_id        = aws_instance.server.id
  port             = 19132
}

resource "aws_lb_target_group_attachment" "minecraft" {
  target_group_arn = module.nlb.target_group_arns[1]
  target_id        = aws_instance.server.id
  port             = 25565
}