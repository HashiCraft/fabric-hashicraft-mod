package com.hashicorp.hashicraft.item;

import com.hashicorp.hashicraft.Mod;
import com.hashicorp.sound.Sounds;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModItems {
  public static final Item VAULT_CARD_ITEM = registerItem("vault_card",
      new VaultCard(new FabricItemSettings().group(ItemGroups.HASHICRAFT)));

  public static final Item HASHICONF_MUSIC_DISC = registerItem("hashiconf_music",
      new HashiConfMusicDisc(7, Sounds.HASHICONF_MUSIC,
          new FabricItemSettings().group(ItemGroups.HASHICRAFT).maxCount(1)));

  public static final Item APP_MINECART_ITEM = registerItem("app_minecart",
      new AppMinecart(new FabricItemSettings().group(ItemGroups.HASHICRAFT)));

  public static final Item APPLICATION_ITEM = registerItem("application",
          new Application(new FabricItemSettings().group(ItemGroups.HASHICRAFT)));
  public static final Item WRENCH_ITEM = registerItem("wrench",
          new WrenchItem(new FabricItemSettings().group(ItemGroups.HASHICRAFT).maxCount(1)));

  private static Item registerItem(String name, Item item) {
    return Registry.register(Registry.ITEM, Mod.identifier(name), item);
  }

  public static void register() {
  }
}