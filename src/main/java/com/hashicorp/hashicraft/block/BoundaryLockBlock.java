package com.hashicorp.hashicraft.block;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.BoundaryLockEntity;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.hashicraft.ui.event.BoundaryLockClicked;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

public class BoundaryLockBlock extends StatefulBlock {
  public static final BooleanProperty POWERED = BooleanProperty.create("powered");
  public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

  protected BoundaryLockBlock(BlockBehaviour.Properties settings) {
    super(settings);
    this.registerDefaultState(
        this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(POWERED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, POWERED);
  }

  @Override
  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
  }

  public boolean isSignalSource(BlockState state) {
    return true;
  }

  public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
    return state.getValue(POWERED) ? 15 : 0;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new BoundaryLockEntity(pos, state);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
    BlockEntity entity = level.getBlockEntity(pos);

    if (entity instanceof BoundaryLockEntity) {
      BoundaryLockEntity lock = (BoundaryLockEntity) entity;

      if (level.isClientSide()) {
        if (stack.is(ModItems.WRENCH_ITEM)) {
          BoundaryLockClicked.EVENT.invoker().interact(lock, () -> {
            lock.markForUpdate();
          });
        }
        return InteractionResult.SUCCESS;
      }
    }

    return InteractionResult.SUCCESS;
  }
}