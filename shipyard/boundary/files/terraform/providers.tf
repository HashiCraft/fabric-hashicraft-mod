terraform {
  required_providers {
    boundary = {
      source  = "hashicorp/boundary"
      version = "1.0.10"
    }
  }
}

provider "boundary" {
  addr             = "http://boundary.container.shipyard.run:9200"
  recovery_kms_hcl = <<EOT
  kms "aead" {
    purpose = "recovery"
    aead_type = "aes-gcm"
    key = "nIRSASgoP91KmaEcg/EAaM4iAkksyB+Lkes0gzrLIRM="
    key_id = "global_recovery"
  }
  EOT
}
