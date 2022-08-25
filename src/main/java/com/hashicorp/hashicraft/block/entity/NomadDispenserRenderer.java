package com.hashicorp.hashicraft.block.entity;

import com.hashicorp.hashicraft.item.ModItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class NomadDispenserRenderer<T extends NomadDispenserEntity> implements BlockEntityRenderer<T> {
    public NomadDispenserRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(NomadDispenserEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Direction direction = entity.getCachedState().get(Properties.HORIZONTAL_FACING);
        matrices.push();

        float xTranslate = 0.0F;
        float yTranslate = 0.7F;
        float zTranslate = 0.0F;

        float scale = 0.4f;
        float rotation = 0.0F;

        switch (direction) {
            case UP:
                break;
            case DOWN:
                break;
            case NORTH:
                xTranslate = 0.5F;
                zTranslate = 0.4F;
                rotation = 180.0F;
                break;
            case SOUTH:
                xTranslate = 0.5F;
                zTranslate = 0.6F;
                rotation = 0.0F;
                break;
            case EAST:
                xTranslate = 0.6F;
                zTranslate = 0.5F;
                rotation = 90.0F;
                break;
            case WEST:
                xTranslate = 0.4F;
                zTranslate = 0.5F;
                rotation = 270.0F;
                break;
        }

        matrices.translate(xTranslate, yTranslate, zTranslate);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotation));

        matrices.scale(scale, scale, scale);

        ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();

        renderer.renderItem(new ItemStack(Items.MINECART, 1),
                ModelTransformation.Mode.GUI,
                light, overlay, matrices, vertexConsumers, 0);
        renderer.renderItem(new ItemStack(ModItems.APPLICATION_ITEM, 1),
                ModelTransformation.Mode.GUI,
                light, overlay, matrices, vertexConsumers, 0);
        renderer.renderItem(new ItemStack(Items.BLACK_DYE, 1),
                ModelTransformation.Mode.GUI,
                light, overlay, matrices, vertexConsumers, 0);

        matrices.pop();
    }

    public void renderItem(Item item, float offset) {}
}
