package com.hashicorp.hashicraft.block;

import java.util.concurrent.ExecutorService;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.NomadServerEntity;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.hashicraft.ui.event.NomadServerClicked;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class NomadServerBlock extends StatefulBlock {
  public static final BooleanProperty POWERED = BooleanProperty.of("powered");
  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  ExecutorService executor;

  public NomadServerBlock(Settings settings) {
    super(settings);
    this.setDefaultState(this.stateManager.getDefaultState()
        .with(FACING, Direction.NORTH)
        .with(POWERED, false));
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(FACING, POWERED);
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
      BlockHitResult hit) {
    ItemStack stack = player.getStackInHand(hand);
    BlockEntity entity = world.getBlockEntity(pos);

    if (entity instanceof NomadServerEntity) {
      NomadServerEntity server = (NomadServerEntity) entity;

      if (world.isClient) {
        if (stack.isOf(ModItems.WRENCH_ITEM)) {
          NomadServerClicked.EVENT.invoker().interact(server, () -> {
            server.markForUpdate();
          });
        }
        return ActionResult.SUCCESS;
      } else {
        if (!stack.isOf(ModItems.WRENCH_ITEM)) {
          if (server.getAddress() == "") {
            player.sendMessage(Text.literal("ERROR - Nomad address is not set"), true);
          }

          boolean created = server.createJob();
          if (created) {
            player.sendMessage(Text.literal("INFO - Nomad job created"), true);
          } else {
            player.sendMessage(Text.literal("ERROR - Could not create Nomad job"), true);
          }
        }
      }
    }

    return ActionResult.SUCCESS;
  }

  @Override
  public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    if (!state.get(POWERED).booleanValue()) {
      return;
    }

    BlockState newState = state.with(POWERED, false);
    world.setBlockState(pos, newState, Block.NOTIFY_ALL);
  }

  public boolean emitsRedstonePower(BlockState state) {
    return true;
  }

  public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
    return state.get(POWERED) != false ? 15 : 0;
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new NomadServerEntity(pos, state);
  }
}
