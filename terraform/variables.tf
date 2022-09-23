variable "region" {
  type        = string
  description = "AWS region"
  default     = "us-west-2"
}

variable "name" {
  type        = string
  description = "Name to prefix all resources"
  default     = "hashicraft"
}

data "aws_availability_zones" "available" {
  filter {
    name   = "opt-in-status"
    values = ["opt-in-not-required"]
  }
}