package com.hashicorp.hashicraft.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

@Environment(EnvType.CLIENT)
// TODO: Fix EntityRenderer - requires 2 type parameters
public class AppMinecartEntityRenderer /* extends EntityRenderer<AppMinecartEntity, AppMinecartRenderState> */ {
  // TODO: Fix EntityRenderer - requires major refactoring for MC 26.2
  /*
  public AppMinecartEntityRenderer(EntityRendererProvider.Context ctx) {
    super(ctx);
  }

  public Identifier getTextureLocation(AppMinecartEntity entity) {
    // TODO: Implement proper texture location for Minecraft 26.x
    return Identifier.withDefaultNamespace("textures/entity/minecart.png");
  }
  */
}
