package com.hashicorp.hashicraft.item;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

public class VaultCard extends Item {
  public VaultCard(Settings settings) {
    super(settings);
  }

  @Override
  public ActionResult useOnBlock(ItemUsageContext context) {
    return ActionResult.SUCCESS;
  }

  // @Override
  // public TypedActionResult<ItemStack> use(World world, PlayerEntity
  // playerEntity, Hand hand) {
  // System.out.println("use");
  // playerEntity.playSound(SoundEvents.BLOCK_ANVIL_HIT, 1.0F, 1.0F);
  // return TypedActionResult.success(playerEntity.getStackInHand(hand));
  // }

  @Override
  public String getTranslationKey() {
    return "item.hashicraft.vault_card";
  }

  @Override
  public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
    if (!itemStack.hasNbt()) {
      tooltip.add(Text.literal("Not valid").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
      return;
    }

    NbtCompound identity = itemStack.getOrCreateNbt();
    String name = identity.getString("name");
    String policies = identity.getString("policy");

    tooltip.add(Text.literal("Name").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
    tooltip.add(Text.literal(name).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    tooltip.add(Text.literal(""));
    tooltip.add(Text.literal("Policies").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
    tooltip.add(Text.literal(policies).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));

    // tooltip.add(Text.literal("Name: " +
    // name).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    // tooltip.add(Text.literal("Access: " +
    // policies).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));

    // tooltip.add(
    // Text.literal("UUID: " + policies.substring(0, 16) +
    // "...").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
  }
}