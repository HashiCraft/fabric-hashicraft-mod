package com.hashicorp.hashicraft.entity;

import com.hashicorp.hashicraft.Mod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

  public static final Identifier APP_MINECART_ID = Mod.identifier("app_minecart");

  public static final EntityType<AppMinecartEntity> APP_MINECART = Registry.register(
      Registries.ENTITY_TYPE,
      APP_MINECART_ID,
      FabricEntityTypeBuilder.<AppMinecartEntity>create(SpawnGroup.MISC, AppMinecartEntity::new)
          .dimensions(EntityDimensions.fixed(1.0f, 1.0f))
          .build());

  public static void register() {
  }
}