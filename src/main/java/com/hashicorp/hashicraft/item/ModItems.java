package com.hashicorp.hashicraft.item;

import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.block.ModBlocks;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {
  public static final Identifier VAULT_CARD_ID = Mod.identifier("vault_card_item");
  public static final Item VAULT_CARD_ITEM = new VaultCardItem(new Item.Settings());

  public static final Identifier APP_MINECART_ID = Mod.identifier("app_minecart_item");
  public static final Item APP_MINECART_ITEM = new AppMinecart(new Item.Settings());

  public static final Identifier APPLICATION_ID = Mod.identifier("application_item");
  public static final Item APPLICATION_ITEM = new Application(new Item.Settings());

  public static final Identifier WRENCH_ID = Mod.identifier("wrench_item");
  public static final Item WRENCH_ITEM = new WrenchItem(new Item.Settings().maxCount(1));

  public static final Identifier CONSUL_RELEASER_ID = Mod.identifier("consul_releaser_item");
  public static Item CONSUL_RELEASER_ITEM = new BlockItem(ModBlocks.CONSUL_RELEASER_BLOCK, new Item.Settings());

  public static final Identifier NOMAD_SERVER_ID = Mod.identifier("nomad_server_item");
  public static Item NOMAD_SERVER_ITEM = new BlockItem(ModBlocks.NOMAD_SERVER_BLOCK, new Item.Settings());

  public static final Identifier NOMAD_DISPENSER_ID = Mod.identifier("nomad_dispenser_item");
  public static Item NOMAD_DISPENSER_ITEM = new BlockItem(ModBlocks.NOMAD_DISPENSER_BLOCK, new Item.Settings());

  public static final Identifier VAULT_DISPENSER_ID = Mod.identifier("vault_dispenser_item");
  public static Item VAULT_DISPENSER_ITEM = new BlockItem(ModBlocks.VAULT_DISPENSER_BLOCK, new Item.Settings());

  public static final Identifier VAULT_LOCK_ID = Mod.identifier("vault_lock_item");
  public static Item VAULT_LOCK_ITEM = new BlockItem(ModBlocks.VAULT_LOCK_BLOCK, new Item.Settings());

  public static final Identifier BOUNDARY_LOCK_ID = Mod.identifier("boundary_lock_item");
  public static Item BOUNDARY_LOCK_ITEM = new BlockItem(ModBlocks.BOUNDARY_LOCK_BLOCK, new Item.Settings());

  public static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP,
      new Identifier(Mod.MOD_ID, "hashicraft"));

  public static void register() {
    Registry.register(Registries.ITEM, APP_MINECART_ID, APP_MINECART_ITEM);
    Registry.register(Registries.ITEM, APPLICATION_ID, APPLICATION_ITEM);

    Registry.register(Registries.ITEM, WRENCH_ID, WRENCH_ITEM);

    Registry.register(Registries.ITEM, CONSUL_RELEASER_ID, CONSUL_RELEASER_ITEM);

    Registry.register(Registries.ITEM, NOMAD_SERVER_ID, NOMAD_SERVER_ITEM);
    Registry.register(Registries.ITEM, NOMAD_DISPENSER_ID, NOMAD_DISPENSER_ITEM);

    Registry.register(Registries.ITEM, VAULT_CARD_ID, VAULT_CARD_ITEM);
    Registry.register(Registries.ITEM, VAULT_DISPENSER_ID, VAULT_DISPENSER_ITEM);
    Registry.register(Registries.ITEM, VAULT_LOCK_ID, VAULT_LOCK_ITEM);

    Registry.register(Registries.ITEM, BOUNDARY_LOCK_ID, BOUNDARY_LOCK_ITEM);

    Registry.register(Registries.ITEM_GROUP, ITEM_GROUP, FabricItemGroup.builder()
        .icon(() -> new ItemStack(ModBlocks.NOMAD_SERVER_BLOCK))
        .displayName(Text.translatable("hashicraft.hashistack"))
        .build());

    ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content -> {
      content.add(VAULT_CARD_ITEM);
      content.add(APP_MINECART_ITEM);
      content.add(APPLICATION_ITEM);
      content.add(WRENCH_ITEM);
      content.add(CONSUL_RELEASER_ITEM);
      content.add(NOMAD_SERVER_ITEM);
      content.add(NOMAD_DISPENSER_ITEM);
      content.add(VAULT_DISPENSER_ITEM);
      content.add(VAULT_LOCK_ITEM);
      content.add(BOUNDARY_LOCK_ITEM);
    });
  }
}