package com.hashicorp.hashicraft.mixin;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapelessRecipe.class)
public abstract class AppMinecartCraftMixin {
  @Shadow
  @Final
  private ItemStack output;

  @Shadow
  @Final
  private Identifier id;

  @Inject(method = "craft", at = @At("HEAD"))
  public void interceptCraft(RecipeInputInventory craftingInventory, DynamicRegistryManager dynamicRegistryManager,
      CallbackInfoReturnable<ItemStack> info) {

    NbtCompound nbtList = new NbtCompound();

    for (int i = 0; i < craftingInventory.size(); i++) {
      ItemStack item = craftingInventory.getStack(i);
      if (item.hasNbt()) {
        nbtList.copyFrom(item.getNbt());
      }
    }

    this.output.setNbt(nbtList);
  }
}
