package com.hashicorp.hashicraft.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.Identifier;

import java.util.List;

public class Recipe {
    private JsonObject json;

    public Recipe(List<Ingredient> ingredients, List<Ingredient> acceptableIngredients, Identifier output) {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shapeless");

        JsonArray ingredientList = new JsonArray();
        for (Ingredient i : ingredients) {
            JsonObject ingredientItem = new JsonObject();
            ingredientItem.addProperty("item", i.item.toString());
            ingredientList.add(ingredientItem);
        }

        JsonArray acceptableIngredientList = new JsonArray();
        for (Ingredient i : acceptableIngredients) {
            JsonObject ingredientItem = new JsonObject();
            ingredientItem.addProperty("item", i.item.toString());
            acceptableIngredientList.add(ingredientItem);
        }
        ingredientList.add(acceptableIngredientList);
        recipe.add("ingredients", ingredientList);

        JsonObject result = new JsonObject();
        result.addProperty("item", output.toString());
        result.addProperty("count", 1);
        recipe.add("result", result);

        this.json = recipe;
    }

    public JsonObject JSON() {
        return this.json;
    }
}
