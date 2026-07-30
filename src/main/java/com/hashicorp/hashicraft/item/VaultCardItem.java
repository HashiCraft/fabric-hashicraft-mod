package com.hashicorp.hashicraft.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.List;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.InteractionResult;

public class VaultCardItem extends Item {
    public VaultCardItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.SUCCESS;
    }

    // Note: getDescriptionId() removed - method is final in MC 26.2
    // Use Item.getDescriptionId() instead

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, java.util.function.Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        if (!itemStack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA)) {
            tooltip.accept(Component.literal("Empty").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
            return;
        }

        // TODO: Fix CustomData - requires proper CustomData API
        CompoundTag identity = new CompoundTag();
        String name = identity.contains("name") ? identity.getString("name").orElse("") : "";
        String policies = identity.contains("policies") ? identity.getString("policies").orElse("") : "";

        tooltip.accept(Component.literal("Name").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
        tooltip.accept(Component.literal(name).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        tooltip.accept(Component.literal(""));
        tooltip.accept(Component.literal("Policies").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
        tooltip.accept(Component.literal(policies).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    @Override
    public Component getName(ItemStack stack) {
        // TODO: Fix CustomData API

        // CompoundTag identity = stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.core.component.CustomData.EMPTY).copyTag();

        CompoundTag identity = new CompoundTag();
        String name = identity.contains("name") ? identity.getString("name").orElse("") : "";

        MutableComponent component = Component.translatable(this.getDescriptionId());

        if (!name.isEmpty()) {
            component.append(Component.literal(" (" + name + ")").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }

        return component;
    }
}