package com.hashicorp.hashicraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import java.util.function.Consumer;
import net.minecraft.world.item.component.TooltipDisplay;

public class Application extends Item {
    public Application(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.SUCCESS;
    }

    // Note: getDescriptionId() removed - method is final in MC 26.2
    // Use Item.getDescriptionId() instead

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        if (!itemStack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA)) {
            tooltip.accept(Component.literal("Not valid").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
            return;
        }

        CartNbtData data = CartNbtData.getCustomNbt(itemStack);

        tooltip.accept(Component.literal("Name").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
        tooltip.accept(Component.literal(data.getName()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        tooltip.accept(Component.literal(""));

        tooltip.accept(Component.literal("Version").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
        tooltip.accept(Component.literal(data.getVersion()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        tooltip.accept(Component.literal(""));

        tooltip.accept(Component.literal("Nomad Deployment").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
        tooltip.accept(Component.literal(data.getNomadDeployment()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        tooltip.accept(Component.literal(""));
    }
}
