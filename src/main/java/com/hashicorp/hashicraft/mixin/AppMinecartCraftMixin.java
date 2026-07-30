package com.hashicorp.hashicraft.mixin;

// TODO: Fix import - import net.minecraft.world.level.inventory.CraftingInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.crafting.ShapelessRecipe;
// TODO: Fix import - import net.minecraft.core.registries.HolderLookup.Provider;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

@Mixin(ShapelessRecipe.class)
public abstract class AppMinecartCraftMixin {
  @Shadow
  @Final
  private ItemStack output;

  @Shadow
  @Final
  private Identifier id;

  @Inject(method = "craft", at = @At("HEAD"))
  public void interceptCraft(CraftingInput craftingInventory, /* HolderLookup.Provider provider, */
      CallbackInfoReturnable<ItemStack> info) {
    // TODO: Fix with correct imports and API
    /*
    CompoundTag nbtList = new CompoundTag();

    for (int i = 0; i < craftingInventory.size(); i++) {
      ItemStack item = craftingInventory.getItem(i);
      if (item.has(DataComponents.CUSTOM_DATA)) {
        nbtList.merge(item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag());
      }
    }

    this.output.set(DataComponents.CUSTOM_DATA, CustomData.of(nbtList));
    */
  }
}
