package com.hashicorp.hashicraft.item;

import com.hashicorp.hashicraft.Mod;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ItemGroups {
  public static final ItemGroup HASHICRAFT = FabricItemGroupBuilder.build(Mod.identifier("hashicraft"),
      () -> new ItemStack(Items.VAULT_CARD_ITEM));
}
