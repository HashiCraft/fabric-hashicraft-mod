package com.hashicorp.hashicraft.item;

import com.hashicorp.hashicraft.entity.AppMinecartEntity;
import com.hashicorp.hashicraft.entity.ModEntities;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class AppMinecart extends Item {

  public AppMinecart(Item.Settings settings) {
    super(settings);
  }

  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    BlockPos blockPos;
    World world = context.getWorld();
    BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());
    if (!blockState.isIn(BlockTags.RAILS)) {
      return ActionResult.FAIL;
    }
    ItemStack itemStack = context.getStack();
    if (!world.isClient) {
      RailShape railShape = blockState.getBlock() instanceof AbstractRailBlock
          ? blockState.get(((AbstractRailBlock) blockState.getBlock()).getShapeProperty())
          : RailShape.NORTH_SOUTH;
      double d = 0.0;
      if (railShape.isAscending()) {
        d = 0.5;
      }
      AppMinecartEntity entity = new AppMinecartEntity(ModEntities.APP_MINECART, world);
      entity.setPos((double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.0625 + d,
          (double) blockPos.getZ() + 0.5);
      if (itemStack.hasCustomName()) {
        entity.setCustomName(itemStack.getName());
      }
      world.spawnEntity(entity);
      world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
    }
    itemStack.decrement(1);
    return ActionResult.success(world.isClient);
  }
}
