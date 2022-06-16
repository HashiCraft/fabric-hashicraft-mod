package com.hashicorp.hashicraft.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class NomadAllocEntityRenderer<T extends NomadAllocEntity> implements BlockEntityRenderer<T> {
  public NomadAllocEntityRenderer(BlockEntityRendererFactory.Context ctx) {
  }

  @Override
  public void render(NomadAllocEntity entity, float tickDelta, MatrixStack matrices,
      VertexConsumerProvider vertexConsumers, int light, int overlay) {
    Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
    String name = entity.getName();

    renderText(matrices, direction, name, 0.0f, 1.4f, -0.5f, 0.02F, 0xFFffffff);
  }

  private void renderText(MatrixStack matrices, Direction direction, String message, float x, float y, float z,
      float scale, int color) {
    float xTranslate = x;
    float zTranslate = z;
    float yTranslate = y;

    Quaternion yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(0.0F);

    switch (direction) {
      case UP:
        break;
      case DOWN:
        break;
      case NORTH:
        xTranslate += 0.5F;
        zTranslate += 0F;
        break;
      case SOUTH:
        xTranslate += 0.5F;
        zTranslate += 1F;
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F);
        break;
      case EAST:
        xTranslate += 1F;
        zTranslate += 0.5F;
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F);
        break;
      case WEST:
        xTranslate += 0F;
        zTranslate += 0.5F;
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0F);
        break;
    }

    Text text = new LiteralText(message);
    matrices.push();
    matrices.translate(xTranslate, yTranslate, zTranslate);
    matrices.scale(-scale, -scale, scale);
    matrices.multiply(yRotation);

    TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    float width = (float) (-textRenderer.getWidth((StringVisitable) text) / 2);
    textRenderer.drawWithShadow(matrices, text, width, 0F, color);
    matrices.pop();

    switch (direction) {
      case UP:
        break;
      case DOWN:
        break;
      case NORTH:
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F);
        break;
      case SOUTH:
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(0.0F);
        break;
      case EAST:
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0F);
        break;
      case WEST:
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F);
        break;
    }

    matrices.push();
    matrices.translate(xTranslate, yTranslate, zTranslate);
    matrices.scale(-scale, -scale, scale);
    matrices.multiply(yRotation);

    textRenderer.drawWithShadow(matrices, text, width, 0F, color);
    matrices.pop();
  }
}
