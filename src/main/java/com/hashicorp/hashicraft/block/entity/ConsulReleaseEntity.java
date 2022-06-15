package com.hashicorp.hashicraft.block.entity;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class ConsulReleaseEntity extends StatefulBlockEntity {
  @Syncable
  public String name = "";

  public ConsulReleaseEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.CONSUL_RELEASE_ENTITY, pos, state, null);
  }

  public ConsulReleaseEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.CONSUL_RELEASE_ENTITY, pos, state, parent);
  }

  public String getName() {
    if (name == null) {
      return "";
    }
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
