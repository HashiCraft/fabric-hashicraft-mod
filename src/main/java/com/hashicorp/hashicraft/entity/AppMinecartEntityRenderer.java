package com.hashicorp.hashicraft.entity;

import org.joml.Quaternionf;

import com.hashicorp.hashicraft.Mod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

@Environment(value = EnvType.CLIENT)
public class AppMinecartEntityRenderer extends EntityRenderer<AppMinecartEntity> {
  private static final Identifier TEXTURE = Mod.identifier("textures/entity/app_minecart.png");
  protected final EntityModel<AppMinecartEntity> model;

  public AppMinecartEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer) {
    super(ctx);
    this.shadowRadius = 0.7f;
    this.model = new AppMinecartEntityModel(ctx.getPart(layer));
  }

  @Override
  public void render(AppMinecartEntity entity, float f, float g, MatrixStack matrixStack,
      VertexConsumerProvider vertexConsumerProvider, int i) {
    super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
    matrixStack.push();
    long l = (long) ((Entity) entity).getId() * 493286711L;
    l = l * l * 4392167121L + l * 98761L;
    float h = (((float) (l >> 16 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
    float j = (((float) (l >> 20 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
    float k = (((float) (l >> 24 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
    matrixStack.translate(h, j, k);
    double d = MathHelper.lerp((double) g, ((AbstractMinecartEntity) entity).lastRenderX,
        ((Entity) entity).getX());
    double e = MathHelper.lerp((double) g, ((AbstractMinecartEntity) entity).lastRenderY,
        ((Entity) entity).getY());
    double m = MathHelper.lerp((double) g, ((AbstractMinecartEntity) entity).lastRenderZ,
        ((Entity) entity).getZ());

    Vec3d vec3d = ((AbstractMinecartEntity) entity).snapPositionToRail(d, e, m);
    if (vec3d != null) {
      Vec3d vec3d2 = ((AbstractMinecartEntity) entity).snapPositionToRailWithOffset(d, e, m, 0.3f);
      Vec3d vec3d3 = ((AbstractMinecartEntity) entity).snapPositionToRailWithOffset(d, e, m, -0.3f);
      if (vec3d2 == null) {
        vec3d2 = vec3d;
      }
      if (vec3d3 == null) {
        vec3d3 = vec3d;
      }
      matrixStack.translate(vec3d.x - d, (vec3d2.y + vec3d3.y) / 2.0 - e, vec3d.z - m);
      Vec3d vec3d4 = vec3d3.add(-vec3d2.x, -vec3d2.y, -vec3d2.z);
      if (vec3d4.length() != 0.0) {
        vec3d4 = vec3d4.normalize();
        f = (float) (Math.atan2(vec3d4.z, vec3d4.x) * 180.0 / Math.PI);
      }
    }
    matrixStack.translate(0.0, 0.0, 0.0);
    matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - f));

    this.model.setAngles(entity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(this.getTexture(entity)));
    this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
    matrixStack.pop();

    Text allocation = entity.getAllocationID();
    Text truncatedAllocation = truncateAllocation(allocation);
    renderAllocation(matrixStack, vertexConsumer, f, truncatedAllocation, 0.0f, 1.85f, 0.0f, 0.02F);

    Text application = entity.getApplication();
    renderName(matrixStack, vertexConsumer, f, application, 0.0f, 1.6f, 0.0f, 0.03F);

    Text version = entity.getVersion();
    renderVersion(matrixStack, vertexConsumer, f, version, 0.0f, 1.3f, 0.0f, 0.02F);
  }

  private Text truncateAllocation(Text allocation) {
    if (allocation != null) {
      String alloc = allocation.getString();
      String truncated = alloc.substring(0, Math.min(alloc.length(), 8));
      return Text.literal(truncated);
    }
    return Text.literal("");
  }

  private void renderAllocation(MatrixStack mat, VertexConsumer consumer, float rotation, Text text, float x,
      float y, float z,
      float scale) {
    float xTranslate = x;
    float zTranslate = z;
    float yTranslate = y;

    Quaternionf yRotation = RotationAxis.POSITIVE_Y.rotationDegrees(-90.0f + rotation);

    MinecraftClient client = MinecraftClient.getInstance();
    TextRenderer textRenderer = client.textRenderer;
    DrawContext context = new DrawContext(client, (Immediate) consumer);
    context.getMatrices().multiplyPositionMatrix(mat.peek().getPositionMatrix());
    var matrices = context.getMatrices();

    matrices.push();
    matrices.translate(xTranslate, yTranslate, zTranslate);
    matrices.scale(-scale, -scale, scale);
    matrices.multiply(yRotation);

    int width = (-textRenderer.getWidth((StringVisitable) text) / 2);
    context.drawTextWithShadow(textRenderer, text, width, 0, 0xFFaaaaaa);
    matrices.pop();

    yRotation = RotationAxis.POSITIVE_Y.rotationDegrees(90.0f + rotation);
    matrices.push();
    matrices.translate(xTranslate, yTranslate, zTranslate);
    matrices.scale(-scale, -scale, scale);
    matrices.multiply(yRotation);

    context.drawTextWithShadow(textRenderer, text, width, 0, 0xFFaaaaaa);
    matrices.pop();
  }

  private void renderName(MatrixStack mat, VertexConsumer consumer, float rotation, Text text, float x, float y,
      float z,
      float scale) {
    float xTranslate = x;
    float zTranslate = z;
    float yTranslate = y;

    Quaternionf yRotation = RotationAxis.POSITIVE_Y.rotationDegrees(-90.0f + rotation);
    MinecraftClient client = MinecraftClient.getInstance();
    TextRenderer textRenderer = client.textRenderer;
    DrawContext context = new DrawContext(client, (Immediate) consumer);
    context.getMatrices().multiplyPositionMatrix(mat.peek().getPositionMatrix());
    var matrices = context.getMatrices();

    matrices.push();
    matrices.translate(xTranslate, yTranslate, zTranslate);
    matrices.scale(-scale, -scale, scale);
    matrices.multiply(yRotation);

    int width = (-textRenderer.getWidth((StringVisitable) text) / 2);
    context.drawTextWithShadow(textRenderer, text, width, 0, 0xFFaaaaaa);
    matrices.pop();

    yRotation = RotationAxis.POSITIVE_Y.rotationDegrees(90.0f + rotation);
    matrices.push();
    matrices.translate(xTranslate, yTranslate, zTranslate);
    matrices.scale(-scale, -scale, scale);
    matrices.multiply(yRotation);

    context.drawTextWithShadow(textRenderer, text, width, 0, 0xFFaaaaaa);
    matrices.pop();
  }

  private void renderVersion(MatrixStack mat, VertexConsumer consumer, float rotation, Text text, float x, float y,
      float z,
      float scale) {
    float xTranslate = x;
    float zTranslate = z;
    float yTranslate = y;

    Quaternionf yRotation = RotationAxis.POSITIVE_Y.rotationDegrees(-90.0f + rotation);
    MinecraftClient client = MinecraftClient.getInstance();
    TextRenderer textRenderer = client.textRenderer;
    DrawContext context = new DrawContext(client, (Immediate) consumer);
    context.getMatrices().multiplyPositionMatrix(mat.peek().getPositionMatrix());
    var matrices = context.getMatrices();

    matrices.push();
    matrices.translate(xTranslate, yTranslate, zTranslate);
    matrices.scale(-scale, -scale, scale);
    matrices.multiply(yRotation);

    int width = (-textRenderer.getWidth((StringVisitable) text) / 2);
    context.drawTextWithShadow(textRenderer, text, width, 0, 0xFFffcc00);
    matrices.pop();

    yRotation = RotationAxis.POSITIVE_Y.rotationDegrees(90.0f + rotation);
    matrices.push();
    matrices.translate(xTranslate, yTranslate, zTranslate);
    matrices.scale(-scale, -scale, scale);
    matrices.multiply(yRotation);

    context.drawTextWithShadow(textRenderer, text, width, 0, 0xFFffcc00);
    matrices.pop();
  }

  @Override
  public Identifier getTexture(AppMinecartEntity entity) {
    return TEXTURE;
  }
}