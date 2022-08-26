package com.hashicorp.hashicraft.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.recipe.Ingredient;
import com.hashicorp.hashicraft.recipe.Recipe;
import com.hashicorp.sound.Sounds;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.hashicorp.hashicraft.Mod.MOD_ID;
import static com.hashicorp.hashicraft.item.Dyes.getAllDyesAsIngredients;

public class ModItems {
    public static final Item VAULT_CARD_ITEM = registerItem("vault_card",
            new VaultCard(new FabricItemSettings().group(ItemGroups.HASHICRAFT)));

    public static final Item HASHICONF_MUSIC_DISC = registerItem("hashiconf_music",
            new HashiConfMusicDisc(7, Sounds.HASHICONF_MUSIC,
                    new FabricItemSettings().group(ItemGroups.HASHICRAFT).maxCount(1)));

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