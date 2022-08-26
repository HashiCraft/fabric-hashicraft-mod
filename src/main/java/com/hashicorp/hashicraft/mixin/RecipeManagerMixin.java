package com.hashicorp.hashicraft.mixin;

import com.google.gson.JsonElement;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static com.hashicorp.hashicraft.item.ModItems.APP_MINECART_ID;
import static com.hashicorp.hashicraft.recipe.AppMineCartRecipe.APP_MINECART_RECIPE;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Inject(method = "apply", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map,
                               ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        map.put(new Identifier(APP_MINECART_ID), APP_MINECART_RECIPE);
    }
}
