package com.hashicorp.hashicraft.block;

import java.util.Random;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.ConsulReleaseEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ConsulRelease extends StatefulBlock {
  public static final BooleanProperty HEALTHY = BooleanProperty.of("healthy");

  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  public ConsulRelease(Settings settings) {
    super(settings);
    this.setDefaultState(
        this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(HEALTHY, false));
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new ConsulReleaseEntity(pos, state);
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(FACING, HEALTHY);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
  }

  public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    if (!state.get(HEALTHY)) {
      for (int i = 0; i < 4; ++i) {
        double x = (double) pos.getX() + 0.2D + (double) random.nextInt(6) / 10;
        double y = (double) pos.getY() + 1.0D;
        double z = (double) pos.getZ() + 0.2D + (double) random.nextInt(6) / 10;
        double vx = 0;
        double vy = ((double) random.nextFloat()) * 0.1D;
        double vz = 0;
        world.addParticle(ParticleTypes.SMOKE, x, y, z, vx, vy, vz);
      }

      for (int i = 0; i < 2; ++i) {
        double x = (double) pos.getX() + 0.2D + (double) random.nextInt(6) / 10;
        double y = (double) pos.getY() + 1.0D;
        double z = (double) pos.getZ() + 0.2D + (double) random.nextInt(6) / 10;
        double vx = 0;
        double vy = ((double) random.nextFloat()) * 0.01D;
        double vz = 0;
        world.addParticle(ParticleTypes.FLAME, x, y, z, vx, vy, vz);
      }
    }
  }
}
