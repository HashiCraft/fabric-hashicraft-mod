package com.hashicorp.hashicraft.recipe;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.hashicorp.hashicraft.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.resources.Identifier;

import static com.hashicorp.hashicraft.item.Dyes.getAllDyesAsIngredients;
import net.minecraft.world.item.Items;

public class AppMineCartRecipe {
  // TODO: Fix recipe - Ingredient API changed in MC 26.2
  // Requires updating to new recipe system
  public static JsonObject APP_MINECART_RECIPE = null;
  
  /*
  public static JsonObject APP_MINECART_RECIPE = new Recipe(
      Lists.newArrayList(
          new Ingredient(Identifier.fromString("minecraft:minecart")),
          new Ingredient(ModItems.APPLICATION_ID)),
      getAllDyesAsIngredients(),
      ModItems.APP_MINECART_ID).JSON();
  */
}