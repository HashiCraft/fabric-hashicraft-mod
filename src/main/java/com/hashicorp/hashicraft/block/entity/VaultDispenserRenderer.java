package com.hashicorp.hashicraft.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

@Environment(EnvType.CLIENT)
public class VaultDispenserRenderer implements BlockEntityRenderer<VaultDispenserEntity, VaultDispenserRenderer.VaultDispenserRenderState> {
  
  public static class VaultDispenserRenderState extends BlockEntityRenderState {
  }
  
  public VaultDispenserRenderer(BlockEntityRendererProvider.Context ctx) {
  }

  @Override
  public VaultDispenserRenderState createRenderState() {
    return new VaultDispenserRenderState();
  }

  @Override
  public void submit(VaultDispenserRenderState state, PoseStack poseStack, 
      SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
    // TODO: Implement rendering for Minecraft 26.x
    // The rendering API has been completely restructured
    // This is a stub implementation to allow compilation
  }
}