package com.hashicorp.hashicraft.block;

import com.hashicorp.hashicraft.block.entity.VaultDispenserEntity;
import com.hashicorp.hashicraft.item.Items;
import com.hashicorp.hashicraft.watcher.Watcher;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
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
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class VaultDispenser extends BlockWithEntity {
  public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  public VaultDispenser(Settings settings) {
    super(settings);
    this.setDefaultState(
        this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(ACTIVE, false));
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new VaultDispenserEntity(pos, state);
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
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
    if (world.isClient)
      return ActionResult.SUCCESS;

    BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof VaultDispenserEntity) {
      VaultDispenserEntity dispenser = (VaultDispenserEntity) blockEntity;

      ItemStack itemStack = new ItemStack(Items.VAULT_CARD_ITEM);
      Direction direction = dispenser.getCachedState().get(FACING);

      String name = player.getName().asString();
      String uuid = player.getUuid().toString();
      String policies = "default,level-1";

      boolean success = Watcher.createUserPass(name, uuid, policies);
      if (!success) {
        player.sendMessage(new LiteralText("ERROR - Could not dispense ID card"), true);
        return ActionResult.SUCCESS;
      }

      NbtCompound identity = itemStack.getOrCreateNbt();
      identity.putString("name", name);
      identity.putString("uuid", uuid);
      identity.putString("policies", policies);
      itemStack.setNbt(identity);

      BlockPointerImpl pointer = new BlockPointerImpl((ServerWorld) world, pos);

      dispenser.dispense(world, pointer, itemStack, 1, direction);
    }
    return ActionResult.SUCCESS;
  }
}
