package com.hashicorp.hashicraft.block.entity;

import com.hashicorp.hashicraft.Mod;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class VaultLockEntityRenderer<T extends VaultLockEntity> implements BlockEntityRenderer<T> {
  public static final Identifier SUCCESS_TEXTURE = Mod.identifier("textures/block/status_success.png");
  public static final Identifier FAILURE_TEXTURE = Mod.identifier("textures/block/status_failure.png");

  public VaultLockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
  }

  @Override
  public void render(VaultLockEntity entity, float tickDelta, MatrixStack matrices,
      VertexConsumerProvider vertexConsumers, int light, int overlay) {
    Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
    if (entity.getStatus().contentEquals("success")) {
      renderStatus(matrices, direction, light, overlay, SUCCESS_TEXTURE);
      renderStatus(matrices, direction.getOpposite(), light, overlay, SUCCESS_TEXTURE);
    } else if (entity.getStatus().contentEquals("failure")) {
      renderStatus(matrices, direction, light, overlay, FAILURE_TEXTURE);
      renderStatus(matrices, direction.getOpposite(), light, overlay, FAILURE_TEXTURE);
    }
  }

  private void renderStatus(MatrixStack matrices, Direction direction, int light, int overlay, Identifier texture) {
    RenderSystem.enableDepthTest();
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, texture);

    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.depthMask(false);

    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferBuilder = tessellator.getBuffer();

    matrices.push();

    float xOffset = 0.0F;
    float zOffset = 0.0F;

    float zTranslate = 0.0F;
    float xTranslate = 0.0F;

    float width = 0.75f;
    float height = 0.75f;

    Quaternion yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(0.0F);

    switch (direction) {
      case NORTH:
        zTranslate = -0.02F;
        zOffset = 1.0F;
        xOffset = 1.0F;
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F);
        break;
      case SOUTH:
        zTranslate = 0.02F;
        break;
      case EAST:
        xTranslate = 0.02F;
        zOffset = 1.0F;
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F);
        break;
      case WEST:
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0F);
        xTranslate = -0.02F;
        xOffset = 1.0F;
        break;
      default:
        break;
    }

    matrices.translate(xTranslate + xOffset, 0.00F, zTranslate + zOffset);
    matrices.multiply(yRotation);

    // Draw face
    Matrix4f matrix4f = matrices.peek().getPositionMatrix();
    bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

    bufferBuilder.vertex(matrix4f, width + 0.125F, 0.125F, 1.0F).texture(1.0F, 1.0F).color(255, 255, 255, 255)
        .light(light).overlay(overlay).next(); // A

    bufferBuilder.vertex(matrix4f, width + 0.125F, height + 0.125F, 1.0F).texture(1.0F, 0.0F)
        .color(255, 255, 255, 255).light(light).overlay(overlay).next(); // B

    bufferBuilder.vertex(matrix4f, 0.125F, height + 0.125F, 1.0F).texture(0.0F, 0.0F).color(255, 255, 255, 255)
        .light(light).overlay(overlay).next(); // C

    bufferBuilder.vertex(matrix4f, 0.125F, 0.125F, 1.0F).texture(0.0F, 1.0F).color(255, 255, 255, 255).light(light)
        .overlay(overlay).next(); // D

    tessellator.draw();
    matrices.pop();

    RenderSystem.disableDepthTest();
    RenderSystem.depthMask(true);
    RenderSystem.disableBlend();
  }
}