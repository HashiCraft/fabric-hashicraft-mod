module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "3.14.4"

  name                 = var.name
  cidr                 = "10.0.0.0/20"
  azs                  = slice(data.aws_availability_zones.available.names, 0, 3)
  private_subnets      = ["10.0.1.0/28", "10.0.2.0/28", "10.0.3.0/28"]
  public_subnets       = ["10.0.4.0/28", "10.0.5.0/28", "10.0.6.0/28"]
  enable_nat_gateway   = true
  single_nat_gateway   = true
  enable_dns_hostnames = true
}