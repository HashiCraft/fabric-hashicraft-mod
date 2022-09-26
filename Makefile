restore:
	docker exec -ti restic.container.shipyard.run restic restore latest --target /

backup:
	docker exec -ti restic.container.shipyard.run restic backup --tag "hashicraft" --tag "manual" /data

restart:
	shipyard taint container.minecraft
	shipyard run fabric-hashicraft-mod/shipyard --vars-file local.shipyardvars