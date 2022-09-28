data "terracurl_request" "grafana_datasource" {
  name    = "datasource"
  url     = "http://${var.grafana_url}/api/datasources/name/${var.grafana_datasource_name}"
  method  = "GET"
  headers = {
    Content-Type = "application/json"
  }
}

resource "grafana_contact_point" "rift_webhook" {
  name = "rift"

  webhook {
    url         = var.rift_url
    http_method = "POST"
  }
}

resource "grafana_notification_policy" "rift" {
  group_by      = ["project"]
  contact_point = grafana_contact_point.rift_webhook.name

  group_interval = "1m"
  group_wait     = "20s"

  policy {
    matcher {
      label = "project"
      match = "="
      value = var.boundary_project
    }

    contact_point = grafana_contact_point.rift_webhook.name
    group_by      = ["targets"]
  }
}

data "terracurl_request" "grafana_webhook" {
  name    = grafana_contact_point.rift_webhook.name
  url     = "http://${var.grafana_url}/api/v1/provisioning/contact-points"
  method  = "GET"
  headers = {
    Content-Type = "application/json"
  }
}

resource "terracurl_request" "grafana_folder" {
  name = "folder"

  # Create
  url          = "http://${var.grafana_url}/api/folders"
  method       = "POST"
  request_body = <<-EOF
  {
    "uid": "${var.grafana_folder_name}",
    "title": "${var.grafana_folder_name}"
  }
  EOF

  headers = {
    Content-Type = "application/json"
  }

  response_codes = [200, 201, 204]

  # Destroy
  destroy_url    = "http://${var.grafana_url}/api/folders/${var.grafana_folder_name}?forceDeleteRules=true"
  destroy_method = "DELETE"

  destroy_headers = {
    Content-Type = "application/json"
  }

  destroy_response_codes = [200, 202, 204]
}

locals {
  grafana_datasource_id = jsondecode(data.terracurl_request.grafana_datasource.response).uid
  grafana_folder_id     = jsondecode(terracurl_request.grafana_folder.response).uid

  # Webhook uid computation.
  webhook            = jsondecode(data.terracurl_request.grafana_webhook.response)
  webhook_index      = try(index(local.webhook.*.name, "rift"), -1)
  grafana_webhook_id = local.webhook_index == -1 ? "null" : local.webhook[local.webhook_index].uid
}

resource "terracurl_request" "grafana_alert_rules" {
  depends_on = [
    terracurl_request.grafana_folder,
    grafana_contact_point.rift_webhook
  ]
  name = "alert-rules"

  # Create
  url          = "http://${var.grafana_url}/api/v1/provisioning/alert-rules"
  method       = "POST"
  request_body = <<-EOF
    {
    "orgID": ${var.grafana_organization},
    "folderUID": "${local.grafana_folder_id}",
    "ruleGroup": "${var.grafana_rule_group}",
    "title": "${var.grafana_rule_title}",
    "condition": "B",
    "data": [
      {
        "refId": "A",
        "queryType": "",
        "relativeTimeRange": {
          "from": 60,
          "to": 0
        },
        "datasourceUid": "${local.grafana_datasource_id}",
        "model": {
          "editorMode": "code",

            "expr": "rate(envoy_cluster_external_upstream_rq{job=\"payments-deployment\",envoy_response_code=\"500\"}[3m])",
          "hide": false,
          "intervalMs": 1000,
          "legendFormat": "__auto",
          "maxDataPoints": 43200,
          "range": true,
          "refId": "A"
        }
      },
      {
        "refId": "B",
        "queryType": "",
        "relativeTimeRange": {
          "from": 0,
          "to": 0
        },
        "datasourceUid": "-100",
        "model": {
          "conditions": [
            {
              "evaluator": {
                "params": [
                  0
                ],
                "type": "gt"
              },
              "operator": {
                "type": "and"
              },
              "query": {
                "params": [
                  "A"
                ]
              },
              "reducer": {
                "params": [],
                "type": "last"
              },
              "type": "query"
            }
          ],
          "datasource": {
            "type": "__expr__",
            "uid": "-100"
          },
          "expression": "A",
          "hide": false,
          "intervalMs": 1000,
          "maxDataPoints": 43200,
          "refId": "B",
          "type": "classic_conditions"
        }
      }
    ],
    "noDataState": "OK",
    "execErrState": "OK",
    "for": "20s",
    "labels": {
      "environment": "${var.boundary_environment}",
      "project": "${var.boundary_project}",
      "targets": "${var.boundary_targets}",
      "teams": "${var.boundary_teams}"
    }
  }
  EOF

  headers = {
    Content-Type = "application/json"
  }

  response_codes = [200, 201, 204]

  # Destroy
  destroy_url    = "http://${var.grafana_url}/api/v1/provisioning/contact-points/${local.grafana_webhook_id}"
  destroy_method = "DELETE"

  destroy_headers = {
    Content-Type = "application/json"
  }

  destroy_response_codes = [200, 202, 204]
}