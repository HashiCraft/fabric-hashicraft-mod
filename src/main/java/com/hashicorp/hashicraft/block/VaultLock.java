package com.hashicorp.hashicraft.block;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.BlockEntities;
import com.hashicorp.hashicraft.block.entity.VaultLockEntity;
import com.hashicorp.hashicraft.events.VaultLockClicked;
import com.hashicorp.hashicraft.item.Items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class VaultLock extends StatefulBlock {
  public static final BooleanProperty POWERED = BooleanProperty.of("powered");

  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  public VaultLock(Settings settings) {
    super(settings);
    this.setDefaultState(
        this.stateManager.getDefaultState()
            .with(FACING, Direction.NORTH)
            .with(POWERED, false));
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new VaultLockEntity(pos, state);
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

  public boolean emitsRedstonePower(BlockState state) {
    return true;
  }

  public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
    BlockEntity blockEntity = world.getBlockEntity(pos);

    if (blockEntity instanceof VaultLockEntity) {
      VaultLockEntity lock = (VaultLockEntity) blockEntity;
      if (lock.getStatus().contentEquals("success")) {
        return 15;
      } else {
        return 0;
      }
    }

    return 0;
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

        if (stack.isOf(Items.VAULT_CARD_ITEM)) {
          NbtCompound identity = stack.getOrCreateNbt();
          String token = identity.getString("token");
          String policy = identity.getString("policy");
          String lockPolicy = lock.getPolicy();

          if (token == null) {
            lock.setStatus("failure");
            player.sendMessage(Text.literal("ACCESS DENIED - You need to be authenticated"), true);
            return ActionResult.SUCCESS;
          }

          boolean access = lock.checkAccess(policy, lockPolicy);
          // boolean access = Watcher.checkAccess(token, lockPolicy);
          System.out.println(access);
          if (access) {
            lock.setStatus("success");
            world.setBlockState(pos, state.with(POWERED, true), Block.NOTIFY_NEIGHBORS);
            return ActionResult.SUCCESS;
          } else {
            lock.setStatus("failure");
            player.sendMessage(Text.literal("ACCESS DENIED - '" + lockPolicy + "' permissions are required"), true);
            return ActionResult.SUCCESS;
          }
        } else if (stack.isOf(net.minecraft.item.Items.CHICKEN)) {
          return ActionResult.SUCCESS;
        } else {
          lock.setStatus("failure");
          player.sendMessage(Text.literal("ACCESS DENIED - That item does not provide access"), true);
          return ActionResult.SUCCESS;
        }
      }
    }
    return ActionResult.SUCCESS;
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
      BlockEntityType<T> type) {
    return checkType(type, BlockEntities.VAULT_LOCK_ENTITY, VaultLockEntity::tick);
  }
}