package com.hashicorp.hashicraft.block;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.NomadDispenserEntity;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.hashicraft.ui.event.NomadDispenserClicked;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class NomadDispenserBlock extends StatefulBlock {
  public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

  protected NomadDispenserBlock(BlockBehaviour.Properties settings) {
    super(settings);
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new NomadDispenserEntity(pos, state);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
    BlockEntity entity = level.getBlockEntity(pos);

    if (entity instanceof NomadDispenserEntity dispenser) {
      Direction direction = dispenser.getBlockState().getValue(FACING);

      if (level.isClientSide()) {
        if (stack.is(ModItems.WRENCH_ITEM)) {
          NomadDispenserClicked.EVENT.invoker().interact(dispenser, dispenser::markForUpdate);
        }
        return InteractionResult.SUCCESS;
      }

      if (!stack.is(ModItems.WRENCH_ITEM)) {
        // TODO: Fix BlockEntity type - BlockSource pointer = new BlockSource((ServerLevel) level, pos, state, dispenser);

        // TODO: Fix dispense method signature - dispenser.dispense(level, pointer, dispenser.getMinecart(), 1, direction);
        // TODO: Fix dispense method signature - dispenser.dispense(level, pointer, dispenser.getDye(), 1, direction);
        // TODO: Fix dispense method signature - dispenser.dispense(level, pointer, dispenser.getApplication(), 1, direction);

        dispenser.changeColors();
      }
    }

    return InteractionResult.SUCCESS;
  }
}
