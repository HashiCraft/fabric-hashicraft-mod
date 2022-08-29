package com.hashicorp.hashicraft.item;

import com.google.common.collect.Lists;
import com.hashicorp.hashicraft.recipe.Ingredient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hashicorp.hashicraft.item.Dyes.COLORS;

public class Application extends Item {
    public Application(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.SUCCESS;
    }

    @Override
    public String getTranslationKey() {
        return "item.hashicraft.application";
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (!itemStack.hasNbt()) {
            tooltip.add(Text.literal("Not valid").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            return;
        }

        NbtCompound identity = itemStack.getOrCreateNbt();
        String name = identity.getString("name");
        String version = identity.getString("version");
        String owner = identity.getString("owner");

        tooltip.add(Text.literal("Name").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
        tooltip.add(Text.literal(name).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(Text.literal(""));

        tooltip.add(Text.literal("Version").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
        tooltip.add(Text.literal(version).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(Text.literal(""));

        tooltip.add(Text.literal("Owner").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
        tooltip.add(Text.literal(owner).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(Text.literal(""));
    }
}
