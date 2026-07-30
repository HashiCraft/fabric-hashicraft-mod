package com.hashicorp.hashicraft.mixin;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.hashicorp.hashicraft.Mod;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

  private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();

  // TODO: Update recipe injection after fixing item registration
  // Modify the map of recipes before the apply method is called by Minecraft
  @ModifyVariable(method = "apply(Ljava/util/Map;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", at = @At("HEAD"))
  protected Map<Identifier, JsonElement> editMap(Map<Identifier, JsonElement> map) {
    LOGGER.info("Applying recipes");
    
    // TODO: Re-enable custom recipe injection
    // Identifier appMinecartId = Mod.identifier("app_minecart_item");
    // map.put(appMinecartId, APP_MINECART_RECIPE);

    return map;
  }
}