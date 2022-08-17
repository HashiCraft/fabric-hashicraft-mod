package com.hashicorp.hashicraft.block;

import com.hashicorp.hashicraft.block.entity.NomadDispenserEntity;
import com.hashicorp.hashicraft.item.ModItems;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class NomadDispenser extends BlockWithEntity {
  public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  private final String version;

  public NomadDispenser(Settings settings, String applicationVersion) {
    super(settings);
    this.setDefaultState(
        this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(ACTIVE, false));
    version = applicationVersion;
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    NomadDispenserEntity dispenser = new NomadDispenserEntity(pos, state);
    dispenser.setVersion(version);
    return dispenser;
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
    if (blockEntity instanceof NomadDispenserEntity dispenser) {

      ItemStack itemStack = new ItemStack(ModItems.APPLICATION_ITEM);
      Direction direction = dispenser.getCachedState().get(FACING);

      String name = player.getName().getString();
      String version = dispenser.getVersion();

      NbtCompound identity = itemStack.getOrCreateNbt();
      identity.putString("name", name);
      identity.putString("version", version);
      itemStack.setNbt(identity);

      BlockPointerImpl pointer = new BlockPointerImpl((ServerWorld) world, pos);

      dispenser.dispense(world, pointer, itemStack, 1, direction);
    }
    return ActionResult.SUCCESS;
  }
}


