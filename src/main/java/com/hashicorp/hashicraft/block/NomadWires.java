package com.hashicorp.hashicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class NomadWires extends Block {
  private static final VoxelShape RAYCAST_SHAPE = createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.1D, 16.0D);

  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  public static final BooleanProperty NORTH_CONNECTED = BooleanProperty.of("north_connected");
  public static final BooleanProperty EAST_CONNECTED = BooleanProperty.of("east_connected");
  public static final BooleanProperty SOUTH_CONNECTED = BooleanProperty.of("south_connected");
  public static final BooleanProperty WEST_CONNECTED = BooleanProperty.of("west_connected");

  public NomadWires(Settings settings) {
    super(settings);
    this.setDefaultState(
        this.stateManager.getDefaultState()
            .with(FACING, Direction.NORTH)
            .with(NORTH_CONNECTED, false)
            .with(EAST_CONNECTED, false)
            .with(SOUTH_CONNECTED, false)
            .with(WEST_CONNECTED, false));
  }

  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
      WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (neighborState.getBlock() == Blocks.NOMAD_SERVER_BLOCK ||
        neighborState.getBlock() == Blocks.NOMAD_CLIENT_BLOCK ||
        neighborState.getBlock() == Blocks.NOMAD_ALLOC_BLOCK ||
        neighborState.getBlock() == Blocks.NOMAD_WIRES_BLOCK ||
        neighborState.getBlock() == Blocks.NOMAD_SPIN_BLOCK ||
        neighborState.getBlock() == Blocks.NOMAD_WHISKERS_BLOCK ||
        neighborState.getBlock() == Blocks.CONSUL_PROXY_BLOCK) {
      switch (direction) {
        case NORTH:
          state = state.with(NomadWires.NORTH_CONNECTED, true);
          break;
        case EAST:
          state = state.with(NomadWires.EAST_CONNECTED, true);
          break;
        case SOUTH:
          state = state.with(NomadWires.SOUTH_CONNECTED, true);
          break;
        case WEST:
          state = state.with(NomadWires.WEST_CONNECTED, true);
          break;
        case UP:
          break;
        case DOWN:
          break;
        default:
          break;
      }
    } else {
      switch (direction) {
        case NORTH:
          state = state.with(NomadWires.NORTH_CONNECTED, false);
          break;
        case EAST:
          state = state.with(NomadWires.EAST_CONNECTED, false);
          break;
        case SOUTH:
          state = state.with(NomadWires.SOUTH_CONNECTED, false);
          break;
        case WEST:
          state = state.with(NomadWires.WEST_CONNECTED, false);
          break;
        case UP:
          break;
        case DOWN:
          break;
        default:
          break;
      }
    }

    return state;
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }

  public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
    return RAYCAST_SHAPE;
  }

  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return RAYCAST_SHAPE;
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    BlockPos pos = ctx.getBlockPos();
    boolean north = isConnected(ctx.getWorld().getBlockState(pos.north()).getBlock());
    boolean east = isConnected(ctx.getWorld().getBlockState(pos.east()).getBlock());
    boolean south = isConnected(ctx.getWorld().getBlockState(pos.south()).getBlock());
    boolean west = isConnected(ctx.getWorld().getBlockState(pos.west()).getBlock());

    return getDefaultState()
        .with(FACING, ctx.getPlayerFacing().getOpposite())
        .with(NORTH_CONNECTED, north)
        .with(EAST_CONNECTED, east)
        .with(SOUTH_CONNECTED, south)
        .with(WEST_CONNECTED, west);
  }

  private boolean isConnected(Block block) {
    return (block == Blocks.NOMAD_SERVER_BLOCK ||
        block == Blocks.NOMAD_CLIENT_BLOCK ||
        block == Blocks.NOMAD_ALLOC_BLOCK ||
        block == Blocks.NOMAD_WIRES_BLOCK ||
        block == Blocks.NOMAD_SPIN_BLOCK ||
        block == Blocks.NOMAD_WHISKERS_BLOCK ||
        block == Blocks.CONSUL_PROXY_BLOCK);
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(FACING, NORTH_CONNECTED, EAST_CONNECTED, SOUTH_CONNECTED, WEST_CONNECTED);
  }
}
