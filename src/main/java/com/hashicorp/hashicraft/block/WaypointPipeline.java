package com.hashicorp.hashicraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class WaypointPipeline extends Block {
  public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  public static final BooleanProperty NORTH_CONNECTED = BooleanProperty.of("north_connected");
  public static final BooleanProperty EAST_CONNECTED = BooleanProperty.of("east_connected");
  public static final BooleanProperty SOUTH_CONNECTED = BooleanProperty.of("south_connected");
  public static final BooleanProperty WEST_CONNECTED = BooleanProperty.of("west_connected");

  public WaypointPipeline(Settings settings) {
    super(settings);
    this.setDefaultState(
        this.stateManager.getDefaultState()
            .with(FACING, Direction.NORTH)
            .with(ACTIVE, false)
            .with(NORTH_CONNECTED, false)
            .with(EAST_CONNECTED, false)
            .with(SOUTH_CONNECTED, false)
            .with(WEST_CONNECTED, false));
  }

  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
      WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (neighborState.getBlock() == ModBlocks.WAYPOINT_SERVER_BLOCK ||
        neighborState.getBlock() == ModBlocks.CONSUL_RELEASER_BLOCK ||
        neighborState.getBlock() == ModBlocks.WAYPOINT_STEP_BLOCK ||
        neighborState.getBlock() == ModBlocks.WAYPOINT_PIPELINE_BLOCK) {
      switch (direction) {
        case NORTH:
          state = state.with(WaypointPipeline.NORTH_CONNECTED, true);
          break;
        case EAST:
          state = state.with(WaypointPipeline.EAST_CONNECTED, true);
          break;
        case SOUTH:
          state = state.with(WaypointPipeline.SOUTH_CONNECTED, true);
          break;
        case WEST:
          state = state.with(WaypointPipeline.WEST_CONNECTED, true);
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
          state = state.with(WaypointPipeline.NORTH_CONNECTED, false);
          break;
        case EAST:
          state = state.with(WaypointPipeline.EAST_CONNECTED, false);
          break;
        case SOUTH:
          state = state.with(WaypointPipeline.SOUTH_CONNECTED, false);
          break;
        case WEST:
          state = state.with(WaypointPipeline.WEST_CONNECTED, false);
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

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(FACING, ACTIVE, NORTH_CONNECTED, EAST_CONNECTED, SOUTH_CONNECTED, WEST_CONNECTED);
  }

  private boolean isConnected(Block block) {
    return (block == ModBlocks.WAYPOINT_SERVER_BLOCK ||
        block == ModBlocks.CONSUL_RELEASER_BLOCK ||
        block == ModBlocks.WAYPOINT_STEP_BLOCK ||
        block == ModBlocks.WAYPOINT_PIPELINE_BLOCK);
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

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
      BlockHitResult hit) {
    world.setBlockState(pos, state.with(ACTIVE, !state.get(ACTIVE)), 3);
    return ActionResult.SUCCESS;
  }
}
