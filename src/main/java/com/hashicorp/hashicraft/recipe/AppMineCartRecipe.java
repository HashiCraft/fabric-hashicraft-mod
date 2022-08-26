package com.hashicorp.hashicraft.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import static com.hashicorp.hashicraft.Mod.MOD_ID;
import static com.hashicorp.hashicraft.item.Dyes.getAllDyesAsIngredients;
import static com.hashicorp.hashicraft.item.ModItems.APPLICATION_ID;
import static com.hashicorp.hashicraft.item.ModItems.APP_MINECART_ID;

public class AppMineCartRecipe {
    public static JsonObject APP_MINECART_RECIPE = new Recipe(
            Lists.newArrayList(
                    new Ingredient(new Identifier("minecraft:minecart")),
                    new Ingredient(new Identifier(MOD_ID + ":" + APPLICATION_ID))
            ),
            getAllDyesAsIngredients(),
            new Identifier(MOD_ID + ":" + APP_MINECART_ID)).JSON();
}
