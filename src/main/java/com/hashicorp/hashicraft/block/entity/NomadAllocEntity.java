package com.hashicorp.hashicraft.block.entity;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class NomadAllocEntity extends StatefulBlockEntity {
  @Syncable
  public String name = "";

  public NomadAllocEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.NOMAD_ALLOC_ENTITY, pos, state, null);
  }

  public NomadAllocEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.NOMAD_ALLOC_ENTITY, pos, state, parent);
  }

  public void setName(String name) {
    this.name = name;
    this.setPropertiesToState();
    this.sync();
  }

  public String getName() {
    if (this.name == null) {
      return "";
    }
    return this.name;
  }
}
