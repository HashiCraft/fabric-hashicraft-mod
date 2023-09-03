package com.hashicorp.hashicraft.block;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.VaultDispenserEntity;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.hashicraft.ui.event.VaultDispenserClicked;
import com.hashicorp.hashicraft.vault.Login;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class VaultDispenserBlock extends StatefulBlock {
  public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

  protected VaultDispenserBlock(Settings settings) {
    super(settings);
    this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
  }

  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new VaultDispenserEntity(pos, state);
  }

  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
      BlockHitResult hit) {
    ItemStack stack = player.getStackInHand(hand);
    BlockEntity entity = world.getBlockEntity(pos);

    if (entity instanceof VaultDispenserEntity) {
      VaultDispenserEntity dispenser = (VaultDispenserEntity) entity;
      Direction direction = dispenser.getCachedState().get(FACING);

      if (world.isClient) {
        if (stack.isOf(ModItems.WRENCH_ITEM)) {
          VaultDispenserClicked.EVENT.invoker().interact(dispenser, () -> {
            dispenser.markForUpdate();
          });
        }
        return ActionResult.SUCCESS;
      } else {
        if (!stack.isOf(ModItems.WRENCH_ITEM)) {
          // Create a new card.
          ItemStack card = new ItemStack(ModItems.VAULT_CARD_ITEM);

          // Login using the user pass.
          Login login = dispenser.login(player);
          if (login == null) {
            player.sendMessage(Text.literal("ERROR - Could not login with user pass"), true);
            return ActionResult.FAIL;
          }

          player.sendMessage(Text.literal("Successfully authenticated"), true);

          // Create an Nbt containing the login details and write it to the card.
          NbtCompound identity = card.getOrCreateNbt();
          identity.putString("name", login.auth.metadata.username);
          identity.putString("token", login.auth.token);
          identity.putString("policies", String.join(", ", login.auth.policies));
          card.setNbt(identity);

          // Dispense a card with the login details.
          BlockPointerImpl pointer = new BlockPointerImpl((ServerWorld) world, pos);
          dispenser.dispense(world, pointer, card, 1, direction);
        }
      }
    }

    return ActionResult.SUCCESS;
  }
}
