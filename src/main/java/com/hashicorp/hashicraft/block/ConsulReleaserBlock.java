package com.hashicorp.hashicraft.block;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.BlockEntities;
import com.hashicorp.hashicraft.block.entity.ConsulReleaserEntity;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.hashicraft.ui.event.ConsulReleaserClicked;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ConsulReleaserBlock extends StatefulBlock {
  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  public ConsulReleaserBlock(Settings settings) {
    super(settings);
    this.setDefaultState(
        this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new ConsulReleaserEntity(pos, state);
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
      BlockHitResult hit) {
    BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof ConsulReleaserEntity releaser) {
      ItemStack stack = player.getStackInHand(hand);

      if (world.isClient) {
        if (stack.isOf(ModItems.WRENCH_ITEM)) {
          ConsulReleaserClicked.EVENT.invoker().interact(releaser, releaser::markForUpdate);
          if (releaser.createRelease()) {
            player.sendMessage(Text.literal("INFO - Release created"), true);
          } else {
            player.sendMessage(Text.literal("ERROR - Release not created"), true);
          }
        }
        return ActionResult.SUCCESS;
      }
    }
    return ActionResult.SUCCESS;
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
      BlockEntityType<T> type) {
    return checkType(type, BlockEntities.CONSUL_RELEASER_ENTITY, ConsulReleaserEntity::tick);
  }
}
