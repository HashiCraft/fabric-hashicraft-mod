package com.hashicorp.hashicraft.entity;

import com.hashicorp.hashicraft.Mod;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class ModEntities {
  public static final EntityType<AppMinecartEntity> APP_MINECART = Registry.register(
      Registry.ENTITY_TYPE,
      Mod.identifier("app_minecart"),
      FabricEntityTypeBuilder.<AppMinecartEntity>create(SpawnGroup.MISC, AppMinecartEntity::new)
          .dimensions(EntityDimensions.fixed(1.0f, 1.0f))
          .build());

  public static void register() {
  }
}