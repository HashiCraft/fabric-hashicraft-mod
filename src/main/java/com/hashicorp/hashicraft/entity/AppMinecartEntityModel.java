package com.hashicorp.hashicraft.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

@Environment(EnvType.CLIENT)
// TODO: Fix EntityModel - requires EntityRenderState type
public class AppMinecartEntityModel /* extends EntityModel<AppMinecartRenderState> */ {
  
  /* public AppMinecartEntityModel(ModelPart root) {
    // TODO: Implement model for Minecraft 26.x
  } */

  public void setupAnim(net.minecraft.client.renderer.entity.state.EntityRenderState state) {
    // TODO: Implement animation
  }

  public void renderToBuffer(com.mojang.blaze3d.vertex.PoseStack poseStack, 
      com.mojang.blaze3d.vertex.VertexConsumer buffer, int packedLight, int packedOverlay, 
      int color) {
    // TODO: Implement rendering
  }
}