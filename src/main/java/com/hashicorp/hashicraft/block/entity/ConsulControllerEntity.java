package com.hashicorp.hashicraft.block.entity;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.hashicorp.hashicraft.block.Blocks;
import com.hashicorp.hashicraft.block.ConsulController;
import com.hashicorp.hashicraft.block.ConsulRelease;
import com.hashicorp.hashicraft.watcher.Release;
import com.hashicorp.hashicraft.watcher.Watcher;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ConsulControllerEntity extends StatefulBlockEntity {
  public ConsulControllerEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.CONSUL_CONTROLLER_ENTITY, pos, state, null);
  }

  public ConsulControllerEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.CONSUL_CONTROLLER_ENTITY, pos, state, parent);
  }

  public void setup() {
  }

  public void destroy() {
    for (Release release : Watcher.getReleases().values()) {
      BlockPos releasePos = release.getPos();
      world.removeBlock(releasePos, false);
      world.removeBlockEntity(releasePos);
    }
  }

  public static void tick(World world, BlockPos blockPos, BlockState state, ConsulControllerEntity entity) {
    if (!world.isClient) {
      entity.update(state);
    }
    StatefulBlockEntity.tick(world, blockPos, state, entity);
  }

  private void update(BlockState state) {
    Direction facing = state.get(ConsulController.FACING);
    for (Release release : Watcher.getReleases().values()) {
      BlockPos releasePos = release.getPos();

      placeRelease(facing, releasePos, release.Name, release.Status, release.DeploymentStatus);
    }
  }

  private void placeRelease(Direction facing, BlockPos pos, String name, String status, String deploymentStatus) {
    BlockState state = Blocks.CONSUL_RELEASE_BLOCK.getDefaultState().with(ConsulController.FACING, facing);
    if (deploymentStatus.contentEquals("strategy_status_failed")) {
      state = state.with(ConsulRelease.HEALTHY, false);
    } else {
      state = state.with(ConsulRelease.HEALTHY, true);
    }

    world.setBlockState(pos, state, Block.NOTIFY_ALL);

    BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof ConsulReleaseEntity) {
      ConsulReleaseEntity releaseEntity = (ConsulReleaseEntity) blockEntity;
      releaseEntity.setName(name);
      releaseEntity.setPropertiesToState();
      releaseEntity.sync();
    }
  }
}
