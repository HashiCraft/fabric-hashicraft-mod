package com.hashicorp.hashicraft.block;

import com.hashicorp.sound.Sounds;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
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

public class Phone extends Block {
  public static final BooleanProperty RINGING = BooleanProperty.of("ringing");
  public static final BooleanProperty CALLING = BooleanProperty.of("calling");

  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  public Phone(Settings settings) {
    super(settings);
    this.setDefaultState(
        this.stateManager.getDefaultState().with(RINGING, false).with(CALLING, false).with(FACING, Direction.NORTH));
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(RINGING, CALLING, FACING);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
      BlockHitResult hit) {

    if (!world.isClient() && hand == Hand.MAIN_HAND) {
      boolean ringing = state.get(RINGING);
      boolean calling = state.get(CALLING);
      if (!ringing && !calling) {
        world.setBlockState(pos, state.with(RINGING, true).with(CALLING, false), 3);
        world.playSound(null, pos, Sounds.PHONE_RINGING, SoundCategory.BLOCKS, 1f, 1f);
      } else if (ringing && !calling) {
        world.setBlockState(pos, state.with(RINGING, false).with(CALLING, true), 3);
        world.playSound(null, pos, Sounds.PHONE_TOUCH, SoundCategory.BLOCKS, 1f, 1f);
      } else {
        world.setBlockState(pos, state.with(RINGING, false).with(CALLING, false), 3);
        world.playSound(null, pos, Sounds.PHONE_TOUCH, SoundCategory.BLOCKS, 1f, 1f);
      }
    }

    return ActionResult.SUCCESS;
  }
}