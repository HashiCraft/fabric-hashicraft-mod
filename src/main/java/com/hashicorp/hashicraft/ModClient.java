package com.hashicorp.hashicraft;

import com.hashicorp.hashicraft.block.ModBlocks;
import com.hashicorp.hashicraft.block.entity.*;
import com.hashicorp.hashicraft.entity.AppMinecartEntity;
import com.hashicorp.hashicraft.entity.AppMinecartEntityModel;
import com.hashicorp.hashicraft.entity.AppMinecartEntityRenderer;
// import com.hashicorp.hashicraft.entity.AppMinecartEntityModel;
// import com.hashicorp.hashicraft.entity.AppMinecartEntityRenderer;
import com.hashicorp.hashicraft.entity.ModEntities;
import com.hashicorp.hashicraft.events.ConsulReleaserClicked;
import com.hashicorp.hashicraft.events.NomadDispenserClicked;
import com.hashicorp.hashicraft.events.VaultLockClicked;
import com.hashicorp.hashicraft.ui.*;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.ActionResult;

@Environment(EnvType.CLIENT)
public class ModClient implements ClientModInitializer {
    public static final EntityModelLayer APP_MINECART_LAYER
            = new EntityModelLayer(Mod.identifier("app_minecart"),
            "main");

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(BlockEntities.NOMAD_SPIN_ENTITY,
                NomadSpinEntityRenderer::new);

        BlockEntityRendererRegistry.register(BlockEntities.NOMAD_WHISKERS_ENTITY,
                NomadWhiskersEntityRenderer::new);

        BlockEntityRendererRegistry.register(BlockEntities.NOMAD_ALLOC_ENTITY,
                NomadAllocEntityRenderer::new);

        BlockEntityRendererRegistry.register(BlockEntities.NOMAD_DISPENSER_ENTITY,
                NomadDispenserRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NOMAD_DISPENSER_BLOCK,
                RenderLayer.getTranslucent());

        BlockEntityRendererRegistry.register(BlockEntities.CONSUL_RELEASER_ENTITY,
                ConsulReleaserEntityRenderer::new);

        BlockEntityRendererRegistry.register(BlockEntities.CONSUL_RELEASE_ENTITY,
                ConsulReleaseEntityRenderer::new);

        BlockEntityRendererRegistry.register(BlockEntities.VAULT_MANAGER_ENTITY,
                VaultManagerEntityRenderer::new);

        BlockEntityRendererRegistry.register(BlockEntities.VAULT_LOCK_ENTITY,
                VaultLockEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CONSUL_PROXY_BLOCK,
                RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CONSUL_CONTROLLER_BLOCK,
                RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NOMAD_WIRES_BLOCK,
                RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NOMAD_SPIN_BLOCK,
                RenderLayer.getTranslucent());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WAYPOINT_PIPELINE_BLOCK,
                RenderLayer.getTranslucent());

        VaultLockClicked.EVENT.register((block, callback) -> {
            VaultLockGui gui = new VaultLockGui(block, callback);
            VaultLockScreen screen = new VaultLockScreen(gui);
            MinecraftClient.getInstance().setScreen(screen);

            return ActionResult.PASS;
        });

        ConsulReleaserClicked.EVENT.register((block, callback) -> {
            ConsulReleaserGui gui = new ConsulReleaserGui(block, callback);
            ConsulReleaserScreen screen = new ConsulReleaserScreen(gui);
            MinecraftClient.getInstance().setScreen(screen);

            return ActionResult.PASS;
        });

        NomadDispenserClicked.EVENT.register((block, callback) -> {
            NomadDispenserGui gui = new NomadDispenserGui(block, callback);
            NomadDispenserScreen screen = new NomadDispenserScreen(gui);
            MinecraftClient.getInstance().setScreen(screen);

            return ActionResult.PASS;
        });

        EntityRendererRegistry.register(ModEntities.APP_MINECART, (context) -> {
            return new AppMinecartEntityRenderer(context, APP_MINECART_LAYER);
        });

        EntityModelLayerRegistry.registerModelLayer(APP_MINECART_LAYER,
                AppMinecartEntityModel::getTexturedModelData);
    }
}
