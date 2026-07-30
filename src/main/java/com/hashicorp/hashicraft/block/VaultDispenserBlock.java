package com.hashicorp.hashicraft.block;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.VaultDispenserEntity;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.hashicraft.ui.event.VaultDispenserClicked;
import com.hashicorp.hashicraft.vault.Login;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class VaultDispenserBlock extends StatefulBlock {
  public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

  protected VaultDispenserBlock(BlockBehaviour.Properties settings) {
    super(settings);
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
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
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new VaultDispenserEntity(pos, state);
  }

  @Override
  protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
    BlockEntity entity = level.getBlockEntity(pos);

    if (entity instanceof VaultDispenserEntity) {
      VaultDispenserEntity dispenser = (VaultDispenserEntity) entity;
      Direction direction = dispenser.getBlockState().getValue(FACING);

      if (level.isClientSide()) {
        if (stack.is(ModItems.WRENCH_ITEM)) {
          // TODO: Fabric API Event - Uncomment after updating Fabric API
          // VaultDispenserClicked.EVENT.invoker().interact(dispenser, () -> {
          //   dispenser.markForUpdate();
          // });
        }
        return InteractionResult.SUCCESS;
      } else {
        if (!stack.is(ModItems.WRENCH_ITEM)) {
          // Create a new card.
          ItemStack card = new ItemStack(ModItems.VAULT_CARD_ITEM);

          // Login using the user pass.
          Login login = dispenser.login(player);
          if (login == null) {
            player.sendSystemMessage(Component.literal("ERROR - Could not login with user pass"));
            return InteractionResult.FAIL;
          }

          player.sendSystemMessage(Component.literal("Successfully authenticated"));

          // Create an Nbt containing the login details and write it to the card.
          CompoundTag identity = card.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
          identity.putString("name", login.auth.metadata.username);
          identity.putString("token", login.auth.token);
          identity.putString("policies", String.join(", ", login.auth.policies));
          card.set(DataComponents.CUSTOM_DATA, CustomData.of(identity));

          // Dispense a card with the login details.
          // TODO: Fix BlockEntity type - BlockSource pointer = new BlockSource((ServerLevel) level, pos, state, dispenser);
          // TODO: Fix dispense method signature - dispenser.dispense(level, pointer, card, 1, direction);
        }
      }
    }

    return InteractionResult.SUCCESS;
  }
}
