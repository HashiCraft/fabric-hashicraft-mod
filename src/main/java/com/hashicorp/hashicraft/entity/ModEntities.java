package com.hashicorp.hashicraft.entity;

import com.hashicorp.hashicraft.Mod;
// TODO: Fabric API - import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class ModEntities {

  public static final Identifier APP_MINECART_ID = Mod.identifier("app_minecart");

  /* TODO: Update Fabric API - Replace with proper EntityType.Builder
  public static final EntityType<AppMinecartEntity> APP_MINECART = Registry.register(
      BuiltInRegistries.ENTITY_TYPE,
      APP_MINECART_ID,
      FabricEntityTypeBuilder.<AppMinecartEntity>create(MobCategory.MISC, AppMinecartEntity::new)
          .dimensions(EntityDimensions.fixed(1.0f, 1.0f))
          .build());
  */
  
  // Temporary placeholder - needs proper EntityType.Builder implementation
  public static final EntityType<AppMinecartEntity> APP_MINECART = null;

  public static void register() {
  }
}
