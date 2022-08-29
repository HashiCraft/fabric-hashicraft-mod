package com.hashicorp.hashicraft.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;

public class WrenchItem extends Item {
    public WrenchItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return ActionResult.SUCCESS;
    }

    @Override
    public String getTranslationKey() {
        return "item.hashicraft.wrench";
    }
}
