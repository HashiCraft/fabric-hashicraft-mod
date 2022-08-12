package com.hashicorp.hashicraft.block.entity;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.hashicorp.hashicraft.watcher.Login;
import com.hashicorp.hashicraft.watcher.Watcher;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VaultManagerEntity extends StatefulBlockEntity {
  @Syncable
  public String status = "";

  @Syncable
  public Integer timer = 0;

  @Syncable
  public boolean inTimer = false;

  public long ticks = 0;

  public VaultManagerEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.VAULT_MANAGER_ENTITY, pos, state, null);
  }

  public VaultManagerEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.VAULT_MANAGER_ENTITY, pos, state, parent);
  }

  public static void tick(World world, BlockPos blockPos, BlockState state, VaultManagerEntity entity) {
    entity.timer = Math.round(entity.ticks++ / 20);
    if (entity.inTimer) {
      if (entity.timer < 1) {
        entity.timer++;
        entity.setPropertiesToState();
        entity.sync();
      } else {
        entity.inTimer = false;
        entity.status = "";
        entity.setPropertiesToState();
        entity.sync();
      }
    }

    StatefulBlockEntity.tick(world, blockPos, state, entity);
  }

  public Login login(String name, String uuid) {
    Login login = Watcher.login(name, uuid);
    return login;
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
    this.setPropertiesToState();
    this.sync();
  }
}
