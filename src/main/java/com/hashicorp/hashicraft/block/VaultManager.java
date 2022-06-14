package com.hashicorp.hashicraft.block;

import com.hashicorp.hashicraft.watcher.Watcher;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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
import net.minecraft.world.World;

public class VaultManager extends Block {
  public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  public VaultManager(Settings settings) {
    super(settings);
    this.setDefaultState(
        this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(ACTIVE, false));
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(FACING, ACTIVE);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
      BlockHitResult hit) {
    String name = player.getName().asString();
    String uuid = player.getUuid().toString();

    String token = Watcher.login(name, uuid);
    if (token == null) {
      player.sendMessage(new LiteralText("ERROR - Could not authorize ID card"), true);
      return ActionResult.SUCCESS;
    }

    ItemStack itemStack = player.getStackInHand(hand);
    NbtCompound identity = itemStack.getOrCreateNbt();
    identity.putString("token", token);
    itemStack.setNbt(identity);

    return ActionResult.SUCCESS;
  }
}
