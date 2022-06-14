package com.hashicorp.hashicraft.block.entity;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VaultLockEntity extends StatefulBlockEntity {
  @Syncable
  public String policy = "default";

  public VaultLockEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.VAULT_LOCK_ENTITY, pos, state, null);
  }

  public VaultLockEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.VAULT_LOCK_ENTITY, pos, state, parent);
  }

  public static void tick(World world, BlockPos blockPos, BlockState state, NomadSpinEntity entity) {
    // Do something every tick.
  }

  public void setPolicy(String policy) {
    this.policy = policy;
  }

  public String getPolicy() {
    return this.policy;
  }
}
