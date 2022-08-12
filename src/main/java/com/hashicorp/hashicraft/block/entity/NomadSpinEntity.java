package com.hashicorp.hashicraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NomadSpinEntity extends BlockEntity {
  public NomadSpinEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.NOMAD_SPIN_ENTITY, pos, state);
  }

  public static void tick(World world, BlockPos blockPos, BlockState state, NomadSpinEntity entity) {
    // Do something every tick.
  }
}
