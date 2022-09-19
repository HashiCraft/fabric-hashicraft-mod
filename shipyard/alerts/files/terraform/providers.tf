terraform {
  required_providers {
    terracurl = {
      source  = "devops-rob/terracurl"
      version = "0.1.1"
    }

    grafana = {
      source  = "grafana/grafana"
      version = "1.28.2"
    }
  }
}

provider "terracurl" {
}

provider "grafana" {
  url  = "http://grafana.container.shipyard.run:3000"
  auth = "admin:admin"
}
