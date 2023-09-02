package com.hashicorp.hashicraft.mixin;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;

import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

import static com.hashicorp.hashicraft.item.ModItems.APP_MINECART_ID;
import static com.hashicorp.hashicraft.recipe.AppMineCartRecipe.APP_MINECART_RECIPE;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

  private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();

  // Modify the map of recipes before the apply method is called by the Minecraft
  @ModifyVariable(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("HEAD"))
  protected Map<Identifier, JsonElement> editMap(Map<Identifier, JsonElement> map) {

    LOGGER.info("Applying recipes");

    map.put(APP_MINECART_ID, APP_MINECART_RECIPE);

    return map;
  }

}
