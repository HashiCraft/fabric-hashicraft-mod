package com.hashicorp.hashicraft.block;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.NomadServerEntity;
import com.hashicorp.hashicraft.item.CartNbtData;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.hashicraft.ui.event.NomadServerClicked;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class NomadServerBlock extends StatefulBlock {
  public static final BooleanProperty POWERED = BooleanProperty.create("powered");
  public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

  public NomadServerBlock(BlockBehaviour.Properties settings) {
    super(settings);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(FACING, Direction.NORTH)
        .setValue(POWERED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, POWERED);
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
    BlockEntity entity = level.getBlockEntity(pos);

    if (entity instanceof NomadServerEntity) {
      NomadServerEntity server = (NomadServerEntity) entity;

      if (level.isClientSide()) {
        if (stack.is(ModItems.WRENCH_ITEM)) {
          NomadServerClicked.EVENT.invoker().interact(server, () -> {
            server.markForUpdate();
          });
        }
        return InteractionResult.SUCCESS;
      } else {
        if (stack.is(ModItems.APP_MINECART_ITEM)) {
          if (server.getAddress() == "") {
            player.sendSystemMessage(Component.literal("ERROR - Nomad address is not set"));
            return InteractionResult.SUCCESS;
          }

          CartNbtData data = CartNbtData.getCustomNbt(stack);

          boolean created = server.createJob(
              data.getName(), data.getVersion(), data.getNomadDeployment());
          if (created) {
            player.sendSystemMessage(Component.literal("INFO - Nomad job created"));
          } else {
            player.sendSystemMessage(Component.literal("ERROR - Could not create Nomad job"));
          }
        }
      }
    }

    return InteractionResult.SUCCESS;
  }

  @Override
  protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    if (!state.getValue(POWERED).booleanValue()) {
      return;
    }

    BlockState newState = state.setValue(POWERED, false);
    level.setBlock(pos, newState, Block.UPDATE_ALL);
  }

  public boolean isSignalSource(BlockState state) {
    return true;
  }

  public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
    return state.getValue(POWERED) != false ? 15 : 0;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new NomadServerEntity(pos, state);
  }
}
