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

  @Syncable
  public String status = "";

  public Integer timer = 0;

  public boolean inTimer = false;

  public long ticks = 0;

  public VaultLockEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.VAULT_LOCK_ENTITY, pos, state, null);
  }

  public VaultLockEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.VAULT_LOCK_ENTITY, pos, state, parent);
  }

  public static void tick(World world, BlockPos blockPos, BlockState state, VaultLockEntity entity) {
    if (world.isClient) {
      entity.timer = Math.round(entity.ticks++ / 20);
      if (entity.inTimer) {
        if (entity.timer < 2) {
          entity.timer++;
          entity.markForUpdate();
        } else {
          entity.inTimer = false;
          entity.status = "";
          entity.markForUpdate();
        }
      }
    }

    StatefulBlockEntity.tick(world, blockPos, state, entity);
  }

  public void setPolicy(String policy) {
    this.policy = policy;
    this.markForUpdate();
  }

  public String getPolicy() {
    if (this.policy == null) {
      return "default";
    }
    return this.policy;
  }

  public String getStatus() {
    if (this.status == null) {
      return "";
    }

    return this.status;
  }

  public void setStatus(String status) {
    this.ticks = 0;
    this.timer = 0;
    this.inTimer = true;
    this.status = status;
    this.markForUpdate();
  }

  public boolean checkAccess(String userPolicy, String lockPolicy) {
    return userPolicy.contains(lockPolicy);
  }
}
