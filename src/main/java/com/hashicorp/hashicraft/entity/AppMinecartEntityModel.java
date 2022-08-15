package com.hashicorp.hashicraft.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

@Environment(value = EnvType.CLIENT)
public class AppMinecartEntityModel extends SinglePartEntityModel<AppMinecartEntity> {

  private final ModelPart root;

  public AppMinecartEntityModel(ModelPart root) {
    this.root = root;
  }

  public static TexturedModelData getTexturedModelData() {
    ModelData modelData = new ModelData();
    ModelPartData modelPartData = modelData.getRoot();
    modelPartData.addChild("bottom",
        ModelPartBuilder.create().uv(0, 0).cuboid(-8.0f, -8.0f, -16.0f, 16.0f, 16.0f, 16.0f),
        ModelTransform.of(0.0f, 0.0f, 0.0f, 1.5707964f, 0.0f, 0.0f));
    return TexturedModelData.of(modelData, 64, 64);
  }

  @Override
  public void setAngles(AppMinecartEntity entity, float limbAngle, float limbDistance, float animationProgress,
      float headYaw,
      float headPitch) {
  }

  @Override
  public ModelPart getPart() {
    return this.root;
  }

}
