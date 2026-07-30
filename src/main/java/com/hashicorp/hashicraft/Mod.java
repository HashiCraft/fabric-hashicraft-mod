package com.hashicorp.hashicraft;

import com.github.hashicraft.stateful.blocks.EntityServerState;
import com.hashicorp.hashicraft.block.ModBlocks;
import com.hashicorp.hashicraft.block.entity.BlockEntities;
import com.hashicorp.hashicraft.entity.ModEntities;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.world.item.Items;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class Mod implements ModInitializer {
  public static final String MOD_ID = "hashicraft";
  public static final Logger LOGGER = LoggerFactory.getLogger("hashicraft");

  @Override
  public void onInitialize() {

    // TODO: Fix ModSounds - // TODO: ModSounds - ModSounds.register();
    ModBlocks.register();
    // ModItems.register(); // Moved to ModClient to avoid duplicate creative tab registration
    BlockEntities.register();

    EntityServerState.RegisterStateUpdates();
  }

  public static Identifier identifier(String path) {
    return Identifier.fromNamespaceAndPath(MOD_ID, path);
  }
}
