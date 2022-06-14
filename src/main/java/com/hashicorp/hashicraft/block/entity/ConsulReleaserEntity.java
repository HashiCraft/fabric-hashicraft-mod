package com.hashicorp.hashicraft.block.entity;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.hashicorp.hashicraft.watcher.Release;
import com.hashicorp.hashicraft.watcher.Watcher;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConsulReleaserEntity extends StatefulBlockEntity {
  @Syncable
  public String application = "payments";

  @Syncable
  public String status = "";

  @Syncable
  public String deploymentStatus = "";

  @Syncable
  public Integer traffic = 0;

  public ConsulReleaserEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.CONSUL_RELEASER_ENTITY, pos, state, null);
  }

  public ConsulReleaserEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.CONSUL_RELEASER_ENTITY, pos, state, parent);
  }

  public static void tick(World world, BlockPos blockPos, BlockState state, ConsulReleaserEntity entity) {
    if (!world.isClient) {
      Release release = Watcher.getRelease(entity.getApplication());
      if (release != null) {
        entity.status = release.Status;
        entity.deploymentStatus = release.DeploymentStatus;
        entity.traffic = release.CandidateTraffic;
        entity.setPropertiesToState();
        entity.sync();
      }
    }

    StatefulBlockEntity.tick(world, blockPos, state, entity);
  }

  public void setApplication(String application) {
    this.application = application;
  }

  public String getApplication() {
    return this.application;
  }

  public String getStatus() {
    return this.status;
  }

  public String getDeploymentStatus() {
    return this.deploymentStatus;
  }

  public Integer getTraffic() {
    return this.traffic;
  }
}
