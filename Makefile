restore:
	docker exec -ti restic.container.shipyard.run restic restore latest --target /

backup:
	docker exec -ti restic.container.shipyard.run restic backup --tag "hashicraft" --tag "manual" /data

restart:
	shipyard taint container.minecraft
	shipyard run fabric-hashicraft-mod/shipyard --vars-file local.shipyardvars

recreate_alert:
    curl -XDELETE http://admin:${GRAFANA_PASSWORD}@grafana.container.shipyard.run:3000/api/v1/provisioning/alert-rules/$(shell curl http://admin::${GRAFANA_PASSWORD}@grafana.container.shipyard.run:3000/api/ruler/grafana/api/v1/rules?subtype=cortex | jq -r '.hashicraft[0].rules[0].grafana_alert.uid')
    cd shipyard/alerts/files/terraform && sudo terraform state rm terracurl_request.grafana_alert_rules
    shipyard taint exec_remote.alerts_setup && shipyard taint template.alerts_setup
    shipyard run