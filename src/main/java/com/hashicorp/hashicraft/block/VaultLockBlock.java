package com.hashicorp.hashicraft.block;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.VaultLockEntity;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.hashicraft.ui.event.VaultLockClicked;
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
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class VaultLockBlock extends StatefulBlock {
  public static final BooleanProperty POWERED = BooleanProperty.of("powered");
  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  protected VaultLockBlock(Settings settings) {
    super(settings);
    this.setDefaultState(
        this.stateManager.getDefaultState()
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
    return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
  }

  public boolean emitsRedstonePower(BlockState state) {
    return true;
  }

  public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
    return state.get(POWERED) != false ? 15 : 0;
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new VaultLockEntity(pos, state);
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
      BlockHitResult hit) {
    ItemStack stack = player.getStackInHand(hand);
    BlockEntity entity = world.getBlockEntity(pos);

    if (entity instanceof VaultLockEntity) {
      VaultLockEntity lock = (VaultLockEntity) entity;

      if (world.isClient) {
        if (stack.isOf(ModItems.WRENCH_ITEM)) {
          VaultLockClicked.EVENT.invoker().interact(lock, () -> {
            lock.markForUpdate();
          });
        }
        return ActionResult.SUCCESS;
      }

      if (stack.isOf(ModItems.VAULT_CARD_ITEM)) {
        NbtCompound identity = stack.getOrCreateNbt();
        String token = identity.getString("token");
        String policy = identity.getString("policy");

        if (token == null) {
          player.sendMessage(Text.literal("ACCESS DENIED - You need to be authenticated"), true);
          return ActionResult.SUCCESS;
        }

        if (policy == null) {
          player.sendMessage(Text.literal("ACCESS DENIED - No policies found"), true);
          return ActionResult.SUCCESS;
        }

        // check the secret access
        if (!lock.checkAccess(token, policy)) {
          player.sendMessage(Text.literal("ACCESS DENIED - You do not have access to the secret"), true);
          return ActionResult.SUCCESS;
        }

        BlockState newState = state.with(POWERED, true);
        world.setBlockState(pos, newState, Block.NOTIFY_ALL);
        world.scheduleBlockTick(new BlockPos(pos), this, 40);
        player.sendMessage(Text.literal("ACCESS GRANTED"), true);
        return ActionResult.SUCCESS;
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
}