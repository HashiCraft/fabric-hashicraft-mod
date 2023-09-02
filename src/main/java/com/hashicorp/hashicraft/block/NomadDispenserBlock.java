package com.hashicorp.hashicraft.block;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.NomadDispenserEntity;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.hashicraft.ui.event.NomadDispenserClicked;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class NomadDispenserBlock extends StatefulBlock {
  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  protected NomadDispenserBlock(Settings settings) {
    super(settings);
    this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new NomadDispenserEntity(pos, state);
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
      BlockHitResult hit) {
    ItemStack stack = player.getStackInHand(hand);
    BlockEntity entity = world.getBlockEntity(pos);

    if (entity instanceof NomadDispenserEntity dispenser) {
      Direction direction = dispenser.getCachedState().get(FACING);

      if (world.isClient) {
        if (stack.isOf(ModItems.WRENCH_ITEM)) {
          NomadDispenserClicked.EVENT.invoker().interact(dispenser, dispenser::markForUpdate);
        }
        return ActionResult.SUCCESS;
      }

      if (!stack.isOf(ModItems.WRENCH_ITEM)) {
        BlockPointerImpl pointer = new BlockPointerImpl((ServerWorld) world, pos);

        dispenser.dispense(world, pointer, dispenser.getMinecart(), 1, direction);
        dispenser.dispense(world, pointer, dispenser.getDye(), 1, direction);
        dispenser.dispense(world, pointer, dispenser.getApplication(), 1, direction);

        dispenser.changeColors();
      }
    }

    return ActionResult.SUCCESS;
  }
}
