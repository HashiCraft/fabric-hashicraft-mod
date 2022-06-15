package com.hashicorp.hashicraft.block;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.BlockEntities;
import com.hashicorp.hashicraft.block.entity.VaultManagerEntity;
import com.hashicorp.hashicraft.item.Items;
import com.hashicorp.hashicraft.watcher.Login;

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
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class VaultManager extends StatefulBlock {
  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  public VaultManager(Settings settings) {
    super(settings);
    this.setDefaultState(
        this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new VaultManagerEntity(pos, state);
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
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
    if (blockEntity instanceof VaultManagerEntity) {
      VaultManagerEntity manager = (VaultManagerEntity) blockEntity;

      if (world.isClient) {
        return ActionResult.SUCCESS;
      }

      ItemStack itemStack = player.getStackInHand(hand);
      if (!itemStack.isOf(Items.VAULT_CARD_ITEM)) {
        manager.setStatus("failure");
        player.sendMessage(new LiteralText("ERROR - This item does not provide an identity"), true);
        return ActionResult.SUCCESS;
      }
      NbtCompound identity = itemStack.getOrCreateNbt();

      String cardName = identity.getString("name");
      String cardUUID = identity.getString("uuid");

      if (cardName == null || cardUUID == null) {
        manager.setStatus("failure");
        player.sendMessage(new LiteralText("ERROR - This is not a valid ID card"), true);
        return ActionResult.SUCCESS;
      }

      String playerName = player.getName().asString();
      String playerUUID = player.getUuid().toString();

      if (!cardName.contentEquals(playerName) || !cardUUID.contentEquals(playerUUID)) {
        manager.setStatus("failure");
        player.sendMessage(new LiteralText("ERROR - This does not seem to be your ID card"), true);
        return ActionResult.SUCCESS;
      }

      Login login = manager.login(cardName, cardUUID);
      if (login == null) {
        manager.setStatus("failure");
        player.sendMessage(new LiteralText("ERROR - Could not authorize ID card"), true);
        return ActionResult.SUCCESS;
      }

      String token = login.auth.token;
      String policy = String.join(",", login.auth.policies);

      manager.setStatus("success");
      identity.putString("token", token);
      identity.putString("policy", policy);
      itemStack.setNbt(identity);
    }

    return ActionResult.SUCCESS;
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
      BlockEntityType<T> type) {
    return world.isClient ? null : checkType(type, BlockEntities.VAULT_MANAGER_ENTITY, VaultManagerEntity::tick);
  }
}
