package com.hashicorp.hashicraft.block;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.BlockEntities;
import com.hashicorp.hashicraft.block.entity.NomadWhiskersEntity;
import com.hashicorp.sound.Sounds;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
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

public class NomadWhiskers extends StatefulBlock {
  public static final BooleanProperty ACTIVE = BooleanProperty.of("active");
  public static final BooleanProperty COUNTDOWN = BooleanProperty.of("countdown");

  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  public NomadWhiskers(Settings settings) {
    super(settings);
    this.setDefaultState(
        this.stateManager.getDefaultState()
            .with(FACING, Direction.NORTH)
            .with(ACTIVE, false)
            .with(COUNTDOWN, false));
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(FACING, ACTIVE, COUNTDOWN);
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
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
    if (blockEntity instanceof NomadWhiskersEntity) {
      if (state.get(ACTIVE)) {
        NomadWhiskersEntity whiskersEntity = (NomadWhiskersEntity) blockEntity;

        String food = whiskersEntity.getCurrentFood();
        Item item = Items.AIR;
        switch (food) {
          case "beef":
            item = Items.BEEF;
            break;
          case "chicken":
            item = Items.CHICKEN;
            break;
          case "vegetable":
            item = Items.CARROT;
            break;
          case "fish":
            item = Items.TROPICAL_FISH;
            break;
        }

        if (player.getMainHandStack().isOf(item)) {
          world.playSound(null, pos, Sounds.CORRECT_ANSWER, SoundCategory.BLOCKS, 0.3f, 1f);
        } else {
          world.playSound(null, pos, Sounds.WRONG_ANSWER, SoundCategory.BLOCKS, 0.3f, 1f);
        }
      } else {
        world.setBlockState(pos, state.with(ACTIVE, true));
      }
    }
    return ActionResult.SUCCESS;
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new NomadWhiskersEntity(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
      BlockEntityType<T> type) {
    return world.isClient ? null : checkType(type, BlockEntities.NOMAD_WHISKERS_ENTITY, NomadWhiskersEntity::tick);
  }
}
