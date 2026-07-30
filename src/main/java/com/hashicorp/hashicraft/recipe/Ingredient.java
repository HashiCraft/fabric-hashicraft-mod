package com.hashicorp.hashicraft.recipe;

import net.minecraft.resources.Identifier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class Ingredient {
    public Identifier item;

    public Ingredient(Identifier item) {
        this.item = item;
    }
}
