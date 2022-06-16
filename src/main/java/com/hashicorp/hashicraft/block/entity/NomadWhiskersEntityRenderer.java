package com.hashicorp.hashicraft.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class NomadWhiskersEntityRenderer<T extends NomadWhiskersEntity> implements BlockEntityRenderer<T> {

  public NomadWhiskersEntityRenderer(BlockEntityRendererFactory.Context ctx) {
  }

  @Override
  public void render(NomadWhiskersEntity entity, float tickDelta, MatrixStack matrices,
      VertexConsumerProvider vertexConsumers, int light, int overlay) {
    Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);

    renderText(matrices, direction, "finicky-whiskers", 0.0f, 1.4f, 0.0f, 0.02F, 0xFFffffff);

    // If we are still in the countdown, draw start sequence.
    if (entity.inCountdown || entity.inEnding) {
      String message = entity.getMessage();
      renderOnscreenText(matrices, direction, message, 0.0f, 0.7f, 0.0f, 0.03F, 0xFFd4ff50);
    }
    // Else draw an icon.
    else if (entity.inProgress) {
      String food = entity.getCurrentFood();
      renderIcon(matrices, vertexConsumers, light, overlay, direction, food);
      renderText(matrices, direction, "" + entity.getScore(), 0.0f, 1.9f, 0.0f, 0.05F, 0xFFd4ff50);
    }
  }

  private void renderOnscreenText(MatrixStack matrices, Direction direction, String message, float x, float y, float z,
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
        xTranslate = 0.5F;
        zTranslate = -0.05F;
        break;
      case SOUTH:
        xTranslate = 0.5F;
        zTranslate = 1.05F;
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F);
        break;
      case EAST:
        xTranslate = 1.05F;
        zTranslate = 0.5F;
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F);
        break;
      case WEST:
        xTranslate = -0.05F;
        zTranslate = 0.5F;
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
    textRenderer.drawWithShadow(matrices, text, width, 0F, 0xFFd4ff50);
    matrices.pop();
  }

  private void renderIcon(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay,
      Direction direction, String icon) {
    float xTranslate = 0.0F;
    float zTranslate = 0.0F;
    float yTranslate = 0.5F;

    Quaternion xRotation = Vec3f.POSITIVE_X.getDegreesQuaternion(0.0F);
    Quaternion yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(0.0F);

    float scale = 0.8F;

    switch (direction) {
      case UP:
        break;
      case DOWN:
        break;
      case NORTH:
        xTranslate += 0.5F;
        zTranslate += -0.00F;
        xRotation = Vec3f.POSITIVE_X.getDegreesQuaternion(180.0F);
        break;
      case SOUTH:
        xTranslate += 0.5F;
        zTranslate += 1.00F;
        xRotation = Vec3f.POSITIVE_X.getDegreesQuaternion(180.0F);
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F);
        break;
      case EAST:
        xTranslate += 1.00F;
        zTranslate += 0.5F;
        xRotation = Vec3f.POSITIVE_X.getDegreesQuaternion(180.0F);
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(-90.0F);
        break;
      case WEST:
        xTranslate += -0.00F;
        zTranslate += 0.5F;
        xRotation = Vec3f.POSITIVE_X.getDegreesQuaternion(180.0F);
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F);
        break;
    }

    ItemStack item = Items.TROPICAL_FISH.getDefaultStack();
    switch (icon) {
      case "beef":
        item = Items.BEEF.getDefaultStack();
        break;
      case "chicken":
        item = Items.CHICKEN.getDefaultStack();
        break;
      case "veg":
        item = Items.CARROT.getDefaultStack();
        break;
      case "fish":
        item = Items.TROPICAL_FISH.getDefaultStack();
        break;
    }

    matrices.push();
    matrices.translate(xTranslate, yTranslate, zTranslate);
    matrices.scale(-scale, -scale, scale);
    matrices.multiply(xRotation);
    matrices.multiply(yRotation);
    MinecraftClient.getInstance().getItemRenderer().renderItem(item,
        ModelTransformation.Mode.GUI,
        light, overlay, matrices, vertexConsumers, 0);
    matrices.pop();
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
        zTranslate += 0.5F;
        break;
      case SOUTH:
        xTranslate += 0.5F;
        zTranslate += 0.5F;
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F);
        break;
      case EAST:
        xTranslate += 0.5F;
        zTranslate += 0.5F;
        yRotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F);
        break;
      case WEST:
        xTranslate += 0.5F;
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