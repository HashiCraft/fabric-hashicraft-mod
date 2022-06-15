package com.hashicorp.hashicraft.block;

import java.util.Random;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.VaultLockEntity;
import com.hashicorp.hashicraft.events.VaultLockClicked;
import com.hashicorp.hashicraft.item.Items;
import com.hashicorp.hashicraft.watcher.Watcher;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class VaultLock extends StatefulBlock {
  public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
  public static final BooleanProperty POWERED = BooleanProperty.of("powered");

  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  public VaultLock(Settings settings) {
    super(settings);
    this.setDefaultState(
        this.stateManager.getDefaultState()
            .with(FACING, Direction.NORTH)
            .with(POWERED, false)
            .with(ACTIVE, false));
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new VaultLockEntity(pos, state);
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(FACING, POWERED, ACTIVE);
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
  }

  public boolean emitsRedstonePower(BlockState state) {
    return true;
  }

  public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
    world.setBlockState(pos, state.with(ACTIVE, false).with(POWERED, false), Block.NOTIFY_LISTENERS);
    this.updateNeighbors(world, pos, state);
  }

  protected void updateNeighbors(World world, BlockPos pos, BlockState state) {
    world.updateNeighborsAlways(pos, this);
    world.updateNeighborsAlways(pos.down(), this);
    world.updateNeighborsAlways(pos.up(), this);
  }

  public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
    return state.get(POWERED) ? 15 : 0;
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
      BlockHitResult hit) {
    BlockEntity blockEntity = world.getBlockEntity(pos);

    if (blockEntity instanceof VaultLockEntity) {
      VaultLockEntity lock = (VaultLockEntity) blockEntity;

      // Only take action if someone is using a keycard.
      ItemStack stack = player.getStackInHand(hand);

      // If we are on the client, check if we need to pop up the UI.
      if (world.isClient) {
        if (stack.isOf(net.minecraft.item.Items.CHICKEN) && player.hasPermissionLevel(4)) {
          VaultLockClicked.EVENT.invoker().interact(lock, () -> {
            lock.markForUpdate();
          });
          return ActionResult.SUCCESS;
        }
      }
      // Else check if we have access.
      else {
        if (stack.isOf(Items.VAULT_CARD_ITEM)) {
          NbtCompound identity = stack.getOrCreateNbt();
          String token = identity.getString("token");
          String policy = lock.getPolicy();
          boolean access = Watcher.checkAccess(token, policy);
          if (access) {
            world.setBlockState(pos, state.with(ACTIVE, true).with(POWERED, true), Block.NOTIFY_NEIGHBORS);
            world.createAndScheduleBlockTick(new BlockPos(pos), this, 20);
            return ActionResult.SUCCESS;
          } else {
            world.setBlockState(pos, state.with(ACTIVE, true).with(POWERED, false), Block.NOTIFY_NEIGHBORS);
            world.createAndScheduleBlockTick(new BlockPos(pos), this, 20);
            player.sendMessage(
                new LiteralText("ACCESS DENIED - '" + policy + "' permissions are required"),
                true);
            return ActionResult.SUCCESS;
          }
        } else if (stack.isOf(net.minecraft.item.Items.CHICKEN)) {
          return ActionResult.SUCCESS;
        } else {
          world.setBlockState(pos, state.with(ACTIVE, true).with(POWERED, false), Block.NOTIFY_NEIGHBORS);
          world.createAndScheduleBlockTick(new BlockPos(pos), this, 20);
          player.sendMessage(new LiteralText("ACCESS DENIED - That item does not provide access"), true);
          return ActionResult.SUCCESS;
        }
      }
    }
    return ActionResult.SUCCESS;
  }
}