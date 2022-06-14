package com.hashicorp.hashicraft.block.entity;

import com.hashicorp.hashicraft.Mod;
import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
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
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ConsulReleaserEntityRenderer<T extends ConsulReleaserEntity> implements BlockEntityRenderer<T> {

  public static final Identifier SUCCESS_TEXTURE = Mod.identifier("textures/block/releaser_success.png");
  public static final Identifier FAILURE_TEXTURE = Mod.identifier("textures/block/releaser_failure.png");

  public ConsulReleaserEntityRenderer(BlockEntityRendererFactory.Context ctx) {
  }

  @Override
  public void render(ConsulReleaserEntity entity, float tickDelta, MatrixStack matrices,
      VertexConsumerProvider vertexConsumers, int light, int overlay) {
    Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);

    String application = entity.getApplication();
    String traffic = String.valueOf(entity.getTraffic()) + "% / " + (100 - entity.getTraffic()) + "%";
    String status = entity.getStatus();
    String deploymentStatus = entity.getDeploymentStatus();

    renderText(matrices, direction, application, 0.0f, 1.5f, 0.0f, 0.015F, 0xFFffffff);

    if (status.contentEquals("state_idle") && deploymentStatus.contentEquals("strategy_status_complete")) {
      renderStatus(matrices, direction, light, overlay, SUCCESS_TEXTURE);
      renderText(matrices, direction, "SUCCESS", 0.0f, 1.3f, 0.0f, 0.03F, 0xFFd4ff50);
    }

    if ((status.contentEquals("state_monitor") || status.contentEquals("state_deploy"))
        && deploymentStatus.contentEquals("strategy_status_progressing")) {
      renderStatus(matrices, direction, light, overlay, SUCCESS_TEXTURE);
      renderText(matrices, direction, traffic, 0.0f, 1.3f, 0.0f, 0.03F, 0xFFd4ff50);
    }

    if ((status.contentEquals("state_monitor") || status.contentEquals("state_deploy"))
        && deploymentStatus.contentEquals("strategy_status_complete")) {
      renderStatus(matrices, direction, light, overlay, SUCCESS_TEXTURE);
      renderText(matrices, direction, "DEPLOYING", 0.0f, 1.3f, 0.0f, 0.03F, 0xFFd4ff50);
    }

    if (status.contentEquals("state_rollback") && deploymentStatus.contentEquals("strategy_status_failed")) {
      renderStatus(matrices, direction, light, overlay, SUCCESS_TEXTURE);
      renderText(matrices, direction, "ROLLBACK", 0.0f, 1.3f, 0.0f, 0.03F, 0xFFd6510f);
    }

    if (status.contentEquals("state_idle") && deploymentStatus.contentEquals("strategy_status_failed")) {
      renderStatus(matrices, direction, light, overlay, SUCCESS_TEXTURE);
      renderText(matrices, direction, "FAILED", 0.0f, 1.3f, 0.0f, 0.03F, 0xFFd6510f);
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