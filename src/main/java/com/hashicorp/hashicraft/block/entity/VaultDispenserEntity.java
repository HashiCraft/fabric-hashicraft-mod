package com.hashicorp.hashicraft.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class VaultDispenserEntity extends BlockEntity {
  public VaultDispenserEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.VAULT_DISPENSER_ENTITY, pos, state);
  }

  public static void tick(World world, BlockPos blockPos, BlockState state, NomadSpinEntity entity) {
    // Do something every tick.
  }

  public void dispense(World world, ItemStack stack, int offset, Direction side) {
    BlockPointerImpl pointer = new BlockPointerImpl(MinecraftClient.getInstance().getServer().getOverworld(), pos);
    double x = pointer.getX() + 0.7D * (double) side.getOffsetX();
    double y = pointer.getY() + 0.7D * (double) side.getOffsetY();
    double z = pointer.getZ() + 0.7D * (double) side.getOffsetZ();

    if (side.getAxis() == Direction.Axis.Y) {
      y -= 0.125D;
    } else {
      y -= 0.15625D;
    }

    ItemEntity itemEntity = new ItemEntity(world, x, y, z, stack);
    double g = world.random.nextDouble() * 0.1D + 0.2D;
    itemEntity.setVelocity(
        world.random.nextGaussian() * 0.007499999832361937D * (double) offset + (double) side.getOffsetX() * g,
        world.random.nextGaussian() * 0.007499999832361937D * (double) offset + 0.20000000298023224D,
        world.random.nextGaussian() * 0.007499999832361937D * (double) offset + (double) side.getOffsetZ() * g);
    world.spawnEntity(itemEntity);
  }
}
