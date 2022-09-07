package com.hashicorp.hashicraft.block.entity;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.consul.Release;
import com.hashicorp.hashicraft.consul.ReleaseStatus;
import com.hashicorp.hashicraft.consul.Releaser;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.hashicorp.hashicraft.block.ConsulReleaserBlock.HEALTHY;

public class ConsulReleaserEntity extends StatefulBlockEntity {
    @Syncable
    private String address = "";

    @Syncable
    public String application = "payments";

    @Syncable
    public String prometheusAddress = "http://localhost:9090";

    @Syncable
    public String nomadDeployment = "payments-deployment";

    @Syncable
    public String nomadNamespace = "default";

    @Syncable
    public String status = "";

    @Syncable
    public String deploymentStatus = "";

    @Syncable
    public Integer traffic = 0;

    private ExecutorService executor;
    private Releaser releaser = new Releaser(address);

    public final static String STATUS_FAILED = "FAILED";
    public final static String STATUS_SUCCESS = "SUCCESS";

    public ConsulReleaserEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.CONSUL_RELEASER_ENTITY, pos, state, null);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        releaser.setAddress(address);
    }

    public String getApplication() {
        return this.application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getPrometheusAddress() {
        return prometheusAddress;
    }

    public void setPrometheusAddress(String prometheusAddress) {
        this.prometheusAddress = prometheusAddress;
    }

    public String getNomadDeployment() {
        return nomadDeployment;
    }

    public void setNomadDeployment(String nomadDeployment) {
        this.nomadDeployment = nomadDeployment;
    }

    public String getNomadNamespace() {
        return nomadNamespace;
    }

    public void setNomadNamespace(String nomadNamespace) {
        this.nomadNamespace = nomadNamespace;
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        if (!world.isClient) {
            this.start();
        }
    }

    @Override
    public void markRemoved() {
        super.markRemoved();
        if (this.hasWorld() && !this.getWorld().isClient) {
            this.stop();
        }
    }

    public synchronized void start() {
        Mod.LOGGER.info("Starting background thread - Consul releaser");
        if (executor == null || executor.isShutdown()) {
            executor = Executors.newFixedThreadPool(1);
            executor.submit(() -> {
                try {
                    while (!executor.isShutdown() && !executor.isTerminated()) {

                        if (!(address == null || address.isEmpty() || address.isBlank())) {
                            releaser.setAddress(address);
                            getReleases();
                        }

                        TimeUnit.SECONDS.sleep(5);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void stop() {
        if (executor != null && !executor.isShutdown()) {
            Mod.LOGGER.info("Stopping background thread");
            executor.shutdown();
        }
    }

    private void syncStatus(ReleaseStatus release) {
        this.status = release.Status;
        this.deploymentStatus = release.DeploymentStatus;
        this.traffic = release.CandidateTraffic;
        this.setPropertiesToState();
        this.sync();
    }

    public boolean createRelease() {
        try {
            releaser.setAddress(address);
            Mod.LOGGER.info("Deleting previous release for Consul releaser");
            releaser.delete(application);
            Mod.LOGGER.info("Update release for Consul releaser");
            releaser.create(new Release().build(
                    application,
                    new Release.NomadConfig(nomadDeployment, nomadNamespace),
                    prometheusAddress));
            ReleaseStatus release = releaser.get(application);
            if (release == null) {
                return false;
            }
            syncStatus(release);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getReleases() {
        try {
            releaser.list().forEach((status) -> {
                if (Objects.equals(status.Name, this.application)) {
                    syncStatus(status);
                    setHealth();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setHealth() {
        BlockState state = this.world.getBlockState(this.getPos());
        state = getStatus().equals(STATUS_FAILED) ? state.with(HEALTHY, false) : state.with(HEALTHY, true);
        world.setBlockState(this.pos, state, Block.NOTIFY_ALL);
    }

    public String getStatus() {
        String message = "IDLE";

        if (status != null) {
            if (status.contentEquals("state_monitor") || status.contentEquals("state_deploy")) {
                if (deploymentStatus.contentEquals("strategy_status_progressing")) {
                    message = traffic + "% / " + (100 - traffic) + "%";
                } else if (deploymentStatus.contentEquals("strategy_status_failing")) {
                    message = "FAILING";
                } else {
                    message = "DEPLOYING";
                }
            } else if (deploymentStatus.contentEquals("strategy_status_complete")) {
                message = STATUS_SUCCESS;
            } else if (deploymentStatus.contentEquals("strategy_status_failed")) {
                if (status.contentEquals("state_rollback")) {
                    message = "ROLLBACK";
                } else {
                    message = STATUS_FAILED;
                }
            }
        }

        return message;
    }
}
