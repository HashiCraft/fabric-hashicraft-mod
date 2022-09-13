package com.hashicorp.hashicraft;

import com.hashicorp.hashicraft.block.ModBlocks;
import com.hashicorp.hashicraft.block.entity.BlockEntities;
import com.hashicorp.hashicraft.block.entity.ConsulReleaserRenderer;
import com.hashicorp.hashicraft.block.entity.VaultDispenserRenderer;
import com.hashicorp.hashicraft.entity.AppMinecartEntityModel;
import com.hashicorp.hashicraft.entity.AppMinecartEntityRenderer;
import com.hashicorp.hashicraft.entity.ModEntities;
import com.hashicorp.hashicraft.ui.*;
import com.hashicorp.hashicraft.ui.event.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.ActionResult;

@Environment(EnvType.CLIENT)
public class ModClient implements ClientModInitializer {
    public static final EntityModelLayer APP_MINECART_LAYER = new EntityModelLayer(Mod.identifier("app_minecart"),
            "main");

    @Override
    public void onInitializeClient() {
        // Nomad Dispenser
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NOMAD_DISPENSER_BLOCK,
                RenderLayer.getTranslucent());

        NomadDispenserClicked.EVENT.register((block, callback) -> {
            NomadDispenserGui gui = new NomadDispenserGui(block, callback);
            NomadDispenserScreen screen = new NomadDispenserScreen(gui);
            MinecraftClient.getInstance().setScreen(screen);

            return ActionResult.PASS;
        });

        // Nomad Server
        NomadServerClicked.EVENT.register((block, callback) -> {
            NomadServerGui gui = new NomadServerGui(block, callback);
            NomadServerScreen screen = new NomadServerScreen(gui);
            MinecraftClient.getInstance().setScreen(screen);

            return ActionResult.PASS;
        });

        // Consul Releaser
        BlockEntityRendererRegistry.register(BlockEntities.CONSUL_RELEASER_ENTITY,
                ConsulReleaserRenderer::new);

        ConsulReleaserClicked.EVENT.register((block, callback) -> {
            ConsulReleaserGui gui = new ConsulReleaserGui(block, callback);
            ConsulReleaserScreen screen = new ConsulReleaserScreen(gui);
            MinecraftClient.getInstance().setScreen(screen);

            return ActionResult.PASS;
        });

        // Vault Dispenser
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.VAULT_DISPENSER_BLOCK,
                RenderLayer.getCutoutMipped());

        BlockEntityRendererRegistry.register(BlockEntities.VAULT_DISPENSER_ENTITY,
                VaultDispenserRenderer::new);

        VaultDispenserClicked.EVENT.register((block, callback) -> {
            VaultDispenserGui gui = new VaultDispenserGui(block, callback);
            VaultDispenserScreen screen = new VaultDispenserScreen(gui);
            MinecraftClient.getInstance().setScreen(screen);

            return ActionResult.PASS;
        });

        // Vault Lock
        VaultLockClicked.EVENT.register((block, callback) -> {
            VaultLockGui gui = new VaultLockGui(block, callback);
            VaultLockScreen screen = new VaultLockScreen(gui);
            MinecraftClient.getInstance().setScreen(screen);

            return ActionResult.PASS;
        });

        // Boundary lock
        BoundaryLockClicked.EVENT.register((block, callback) -> {
            BoundaryLockGui gui = new BoundaryLockGui(block, callback);
            BoundaryLockScreen screen = new BoundaryLockScreen(gui);
            MinecraftClient.getInstance().setScreen(screen);

            return ActionResult.PASS;
        });

        // App Minecart
        EntityRendererRegistry.register(ModEntities.APP_MINECART, (context) -> {
            return new AppMinecartEntityRenderer(context, APP_MINECART_LAYER);
        });

        EntityModelLayerRegistry.registerModelLayer(APP_MINECART_LAYER,
                AppMinecartEntityModel::getTexturedModelData);
    }
}
