package com.hashicorp.hashicraft;

import com.github.hashicraft.stateful.blocks.EntityServerState;
import com.hashicorp.hashicraft.block.ModBlocks;
import com.hashicorp.hashicraft.block.entity.BlockEntities;
import com.hashicorp.hashicraft.entity.ModEntities;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.sound.ModSounds;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mod implements ModInitializer {
  public static final String MOD_ID = "hashicraft";
  public static final Logger LOGGER = LoggerFactory.getLogger("hashicraft");

  @Override
  public void onInitialize() {

    ModSounds.register();
    ModBlocks.register();
    ModItems.register();
    BlockEntities.register();
    ModEntities.register();

    EntityServerState.RegisterStateUpdates();
  }

  public static Identifier identifier(String path) {
    return new Identifier(MOD_ID, path);
  }
}
