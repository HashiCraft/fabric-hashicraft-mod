package com.hashicorp.hashicraft.item;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

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

        NbtData data = NbtData.getCustomNbt(itemStack);

        tooltip.add(Text.literal("Name").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
        tooltip.add(Text.literal(data.getName()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(Text.literal(""));

        tooltip.add(Text.literal("Version").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
        tooltip.add(Text.literal(data.getVersion()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(Text.literal(""));

        tooltip.add(Text.literal("Nomad Deployment").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
        tooltip.add(Text.literal(data.getNomadDeployment()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(Text.literal(""));
    }
}
