package com.hashicorp.hashicraft.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class NomadSpinEntityRenderer<T extends NomadSpinEntity> implements BlockEntityRenderer<T> {

  public NomadSpinEntityRenderer(BlockEntityRendererFactory.Context ctx) {
  }

  @Override
  public void render(NomadSpinEntity entity, float tickDelta, MatrixStack matrices,
      VertexConsumerProvider vertexConsumers, int light, int overlay) {
    matrices.push();

    float xTranslate = 0.5F;
    float yTranslate = 0.8F;
    float zTranslate = 0.5F;

    float scale = 0.7F;
    float speed = 8;

    yTranslate = yTranslate + (float) (Math.sin((entity.getWorld().getTime() + tickDelta) / 10.0) / 8.0);
    matrices.translate(xTranslate, yTranslate, zTranslate);
    matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((entity.getWorld().getTime()
        + tickDelta) * speed));
    matrices.scale(scale, scale, scale);

    MinecraftClient.getInstance().getItemRenderer().renderItem(Items.EMERALD_BLOCK.getDefaultStack(),
        ModelTransformation.Mode.GUI,
        light, overlay, matrices, vertexConsumers, 0);

    matrices.pop();
  }
}
