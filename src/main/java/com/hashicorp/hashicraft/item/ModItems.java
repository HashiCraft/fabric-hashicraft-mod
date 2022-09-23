package com.hashicorp.hashicraft.item;

import com.hashicorp.hashicraft.Mod;
// import com.hashicorp.sound.ModSounds;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModItems {
        public static final Item VAULT_CARD_ITEM = registerItem("vault_card",
                        new VaultCardItem(new FabricItemSettings().group(ItemGroups.HASHICRAFT)));

        // public static final Item HASHICONF_MUSIC_DISC =
        // registerItem("hashiconf_music",
        // new HashiConfMusicDisc(7, ModSounds.HASHICONF_MUSIC,
        // new FabricItemSettings().group(ItemGroups.HASHICRAFT).maxCount(1)));

        public static final String APP_MINECART_ID = "app_minecart";

        public static final Item APP_MINECART_ITEM = registerItem(APP_MINECART_ID,
                        new AppMinecart(new FabricItemSettings().group(ItemGroups.HASHICRAFT)));

        public static final String APPLICATION_ID = "application";
        public static final Item APPLICATION_ITEM = registerItem(APPLICATION_ID,
                        new Application(new FabricItemSettings().group(ItemGroups.HASHICRAFT)));
        public static final Item WRENCH_ITEM = registerItem("wrench",
                        new WrenchItem(new FabricItemSettings().group(ItemGroups.HASHICRAFT).maxCount(1)));

        private static Item registerItem(String name, Item item) {
                return Registry.register(Registry.ITEM, Mod.identifier(name), item);
        }

        public static void register() {
        }
}