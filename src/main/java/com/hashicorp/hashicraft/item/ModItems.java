package com.hashicorp.hashicraft.item;

import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.block.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.function.Function;

public class ModItems {
  public static final Item VAULT_CARD_ITEM = register("vault_card_item",
      settings -> new VaultCardItem(settings),
      new Item.Properties());

  public static final Item APP_MINECART_ITEM = register("app_minecart_item",
      settings -> new AppMinecart(settings),
      new Item.Properties());

  public static final Item APPLICATION_ITEM = register("application_item",
      settings -> new Application(settings),
      new Item.Properties());

  public static final Item WRENCH_ITEM = register("wrench_item",
      settings -> new WrenchItem(settings),
      new Item.Properties().stacksTo(1));

  public static final Item CONSUL_RELEASER_ITEM = register("consul_releaser_item",
      settings -> new BlockItem(ModBlocks.CONSUL_RELEASER_BLOCK, settings),
      new Item.Properties());

  public static final Item NOMAD_SERVER_ITEM = register("nomad_server_item",
      settings -> new BlockItem(ModBlocks.NOMAD_SERVER_BLOCK, settings),
      new Item.Properties());

  public static final Item NOMAD_DISPENSER_ITEM = register("nomad_dispenser_item",
      settings -> new BlockItem(ModBlocks.NOMAD_DISPENSER_BLOCK, settings),
      new Item.Properties());

  public static final Item VAULT_DISPENSER_ITEM = register("vault_dispenser_item",
      settings -> new BlockItem(ModBlocks.VAULT_DISPENSER_BLOCK, settings),
      new Item.Properties());

  public static final Item VAULT_LOCK_ITEM = register("vault_lock_item",
      settings -> new BlockItem(ModBlocks.VAULT_LOCK_BLOCK, settings),
      new Item.Properties());

  public static final Item BOUNDARY_LOCK_ITEM = register("boundary_lock_item",
      settings -> new BlockItem(ModBlocks.BOUNDARY_LOCK_BLOCK, settings),
      new Item.Properties());

  public static final ResourceKey<CreativeModeTab> ITEM_GROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
      Identifier.parse(Mod.MOD_ID + ":hashicraft"));

  private static Item register(String path,
                               Function<Item.Properties, Item> factory,
                               Item.Properties settings) {
    Identifier identifier = Mod.identifier(path);
    ResourceKey<Item> registryKey = ResourceKey.create(Registries.ITEM, identifier);
    return Registry.register(BuiltInRegistries.ITEM, registryKey, factory.apply(settings.setId(registryKey)));
  }

  public static void register() {
    // Items are already registered during static initialization
    
    // Register creative mode tab using native Minecraft API (1.21.4+)
    Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ITEM_GROUP, 
        CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ModBlocks.NOMAD_SERVER_BLOCK))
            .title(Component.translatable("itemGroup.hashicraft.hashistack"))
            .displayItems((parameters, output) -> {
              output.accept(VAULT_CARD_ITEM);
              output.accept(APP_MINECART_ITEM);
              output.accept(APPLICATION_ITEM);
              output.accept(WRENCH_ITEM);
              output.accept(CONSUL_RELEASER_ITEM);
              output.accept(NOMAD_SERVER_ITEM);
              output.accept(NOMAD_DISPENSER_ITEM);
              output.accept(VAULT_DISPENSER_ITEM);
              output.accept(VAULT_LOCK_ITEM);
              output.accept(BOUNDARY_LOCK_ITEM);
            })
            .build());
  }
}