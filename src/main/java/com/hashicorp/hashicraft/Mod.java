package com.hashicorp.hashicraft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hashicraft.stateful.blocks.EntityServerState;
import com.hashicorp.hashicraft.block.ModBlocks;
import com.hashicorp.hashicraft.block.entity.BlockEntities;
import com.hashicorp.hashicraft.entity.ModEntities;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.hashicraft.watcher.Watcher;
import com.hashicorp.sound.ModSounds;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.Identifier;

public class Mod implements ModInitializer {
	public static final String MOD_ID = "hashicraft";
	public static final Logger LOGGER = LoggerFactory.getLogger("hashicraft");

	@Override
	public void onInitialize() {
		EntityServerState.RegisterStateUpdates();

		ModSounds.register();
		ModBlocks.register();
		ModItems.register();
		BlockEntities.register();
		ModEntities.register();

		ServerLifecycleEvents.SERVER_STARTED.register(Watcher::Start);
	}

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}
}
