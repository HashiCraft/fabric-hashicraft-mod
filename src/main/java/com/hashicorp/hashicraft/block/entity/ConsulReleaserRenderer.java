package com.hashicorp.hashicraft.block.entity;

import com.hashicorp.hashicraft.Mod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Properties;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.hashicorp.hashicraft.block.entity.ConsulReleaserEntity.*;

@Environment(EnvType.CLIENT)
public class ConsulReleaserRenderer<T extends ConsulReleaserEntity> implements BlockEntityRenderer<T> {
    public static final Identifier SUCCESS_TEXTURE = Mod.identifier("textures/block/status_success.png");
    public static final Identifier FAILURE_TEXTURE = Mod.identifier("textures/block/status_failure.png");
    public static final Identifier PROGRESS_TEXTURE = Mod.identifier("textures/block/status_progress.png");

    private static final float width = 0.75f;
    private static final float height = 0.75f;

    private int spinnerStart = 0;
    private int frameCount = 0;
    private static final int FRAME_INTERVAL = 32;
    private static final int NUM_VERTICES = 4;

    private static final List<Coordinates> VERTICES = Arrays.asList(
            new Coordinates(width + 0.125F, 0.125F),
            new Coordinates(width + 0.125F, height + 0.125F),
            new Coordinates(0.125F, height + 0.125F),
            new Coordinates(0.125F, 0.125F)
    );

    private static final List<TextureUV> UV_COORDINATES = Arrays.asList(
            new TextureUV(1.0F, 1.0F),
            new TextureUV(1.0F, 0.0F),
            new TextureUV(0.0F, 0.0F),
            new TextureUV(0.0F, 1.0F)
    );

    public ConsulReleaserRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    private static class Coordinates {
        float width;
        float height;

        public Coordinates(float width, float height) {
            this.width = width;
            this.height = height;
        }
    }

    public static class TextureUV {
        float u;
        float v;

        public TextureUV(float u, float v) {
            this.u = u;
            this.v = v;
        }
    }

    @Override
    public void render(ConsulReleaserEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);

        String application = entity.getApplication();
        renderText(matrices, direction, application, 0.0f, 1.5f, 0.0f, 0.015F, 0xFFffffff);

        String message = entity.getStatus();

        int color = 0xFFffffff;

        if (message.contentEquals(STATUS_SUCCESS)) {
            color = 0xFFd4ff50;
            renderStatus(matrices, direction, light, overlay, SUCCESS_TEXTURE);
        } else if (message.contentEquals(STATUS_FAILED)) {
            color = 0xFFd6510f;
            renderStatus(matrices, direction, light, overlay, FAILURE_TEXTURE);
        } else if (!message.contentEquals(STATUS_IDLE)) {
            renderStatus(matrices, direction, light, overlay, PROGRESS_TEXTURE);
        }

        renderText(matrices, direction, message, 0.0f, 1.3f, 0.0f, 0.03F, color);
    }

    private void countFrames() {
        if (frameCount == FRAME_INTERVAL) {
            spinnerStart = spinnerStart < NUM_VERTICES ? spinnerStart + 1 : 0;
            frameCount = 0;
        } else {
            frameCount = frameCount + 1;
        }
    }

    private List<TextureUV> rotateTexture() {
        final int TOTAL_COORDINATES = UV_COORDINATES.size();
        List<TextureUV> spinner = new ArrayList<>(TOTAL_COORDINATES);
        for (int i = spinnerStart; i < NUM_VERTICES + spinnerStart; i++) {
            spinner.add(UV_COORDINATES.get(i % TOTAL_COORDINATES));
        }
        return spinner;
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

        List<TextureUV> textureCoordinates = UV_COORDINATES;
        if (texture.equals(PROGRESS_TEXTURE)) {
            textureCoordinates = rotateTexture();
            countFrames();
        }

        // Draw face
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        bufferBuilder.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        bufferBuilder.vertex(matrix4f, VERTICES.get(0).width, VERTICES.get(0).height, 1.0F)
                .texture(textureCoordinates.get(0).u, textureCoordinates.get(0).v)
                .color(255, 255, 255, 255)
                .light(light).overlay(overlay).next(); // A

        bufferBuilder.vertex(matrix4f, VERTICES.get(1).width, VERTICES.get(1).height, 1.0F)
                .texture(textureCoordinates.get(1).u, textureCoordinates.get(1).v)
                .color(255, 255, 255, 255).light(light).overlay(overlay).next(); // B

        bufferBuilder.vertex(matrix4f, VERTICES.get(2).width, VERTICES.get(2).height, 1.0F)
                .texture(textureCoordinates.get(2).u, textureCoordinates.get(2).v)
                .color(255, 255, 255, 255)
                .light(light).overlay(overlay).next(); // C

        bufferBuilder.vertex(matrix4f, VERTICES.get(3).width, VERTICES.get(3).height, 1.0F)
                .texture(textureCoordinates.get(3).u, textureCoordinates.get(3).v)
                .color(255, 255, 255, 255).light(light)
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

        Text text = Text.literal(message);
        matrices.push();
        matrices.translate(xTranslate, yTranslate, zTranslate);
        matrices.scale(-scale, -scale, scale);
        matrices.multiply(yRotation);

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
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