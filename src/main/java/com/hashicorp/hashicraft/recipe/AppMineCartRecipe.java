package com.hashicorp.hashicraft.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.hashicorp.hashicraft.item.ModItems;

import net.minecraft.util.Identifier;

import static com.hashicorp.hashicraft.item.Dyes.getAllDyesAsIngredients;

public class AppMineCartRecipe {
  public static JsonObject APP_MINECART_RECIPE = new Recipe(
      Lists.newArrayList(
          new Ingredient(new Identifier("minecraft:minecart")),
          new Ingredient(ModItems.APPLICATION_ID)),
      getAllDyesAsIngredients(),
      ModItems.APP_MINECART_ID).JSON();
}
