package com.hashicorp.hashicraft.block;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.ConsulReleaserEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

public class ConsulReleaserBlock extends StatefulBlock {
  public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final BooleanProperty HEALTHY = BooleanProperty.create("healthy");

  public ConsulReleaserBlock(BlockBehaviour.Properties settings) {
    super(settings);
    this.registerDefaultState(
        this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HEALTHY, true));
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new ConsulReleaserEntity(pos, state);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, HEALTHY);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
  }

  @Override
  public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
    if (!state.getValue(HEALTHY)) {
      for (int i = 0; i < 4; ++i) {
        double x = (double) pos.getX() + 0.2D + (double) random.nextInt(6) / 10;
        double y = (double) pos.getY() + 1.0D;
        double z = (double) pos.getZ() + 0.2D + (double) random.nextInt(6) / 10;
        double vx = 0;
        double vy = ((double) random.nextFloat()) * 0.1D;
        double vz = 0;
        level.addParticle(net.minecraft.core.particles.ParticleTypes.SMOKE, x, y, z, vx, vy, vz);
      }

      for (int i = 0; i < 2; ++i) {
        double x = (double) pos.getX() + 0.2D + (double) level.getRandom().nextInt(6) / 10;
        double y = (double) pos.getY() + 1.0D;
        double z = (double) pos.getZ() + 0.2D + (double) level.getRandom().nextInt(6) / 10;
        double vx = 0;
        double vy = ((double) level.getRandom().nextFloat()) * 0.01D;
        double vz = 0;
        level.addParticle(ParticleTypes.FLAME, x, y, z, vx, vy, vz);
      }
    }
  }
}
