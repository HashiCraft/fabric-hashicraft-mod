terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.31.0"
    }
  }

  cloud {
    organization = "hashicorp-team-da-beta"

    workspaces {
      name = "fabric-hashicraft-mod"
    }
  }
}

provider "aws" {
  region = var.region

  default_tags {
    tags = {
      Project = var.name
    }
  }
}