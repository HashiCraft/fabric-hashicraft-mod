package com.hashicorp.hashicraft;

import com.hashicorp.hashicraft.block.Blocks;
import com.hashicorp.hashicraft.block.entity.BlockEntities;
import com.hashicorp.hashicraft.block.entity.ConsulReleaseEntityRenderer;
import com.hashicorp.hashicraft.block.entity.ConsulReleaserEntityRenderer;
import com.hashicorp.hashicraft.block.entity.NomadAllocEntityRenderer;
import com.hashicorp.hashicraft.block.entity.NomadSpinEntityRenderer;
import com.hashicorp.hashicraft.block.entity.NomadWhiskersEntityRenderer;
import com.hashicorp.hashicraft.block.entity.VaultLockEntityRenderer;
import com.hashicorp.hashicraft.block.entity.VaultManagerEntityRenderer;
import com.hashicorp.hashicraft.events.ConsulReleaserClicked;
import com.hashicorp.hashicraft.events.VaultLockClicked;
import com.hashicorp.hashicraft.ui.ConsulReleaserGui;
import com.hashicorp.hashicraft.ui.ConsulReleaserScreen;
import com.hashicorp.hashicraft.ui.VaultLockGui;
import com.hashicorp.hashicraft.ui.VaultLockScreen;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.ActionResult;

@Environment(EnvType.CLIENT)
public class ModClient implements ClientModInitializer {
        @Override
        public void onInitializeClient() {
                BlockEntityRendererRegistry.register(BlockEntities.NOMAD_SPIN_ENTITY,
                                NomadSpinEntityRenderer::new);

                BlockEntityRendererRegistry.register(BlockEntities.NOMAD_WHISKERS_ENTITY,
                                NomadWhiskersEntityRenderer::new);

                BlockEntityRendererRegistry.register(BlockEntities.NOMAD_ALLOC_ENTITY,
                                NomadAllocEntityRenderer::new);

                BlockEntityRendererRegistry.register(BlockEntities.CONSUL_RELEASER_ENTITY,
                                ConsulReleaserEntityRenderer::new);

                BlockEntityRendererRegistry.register(BlockEntities.CONSUL_RELEASE_ENTITY,
                                ConsulReleaseEntityRenderer::new);

                BlockEntityRendererRegistry.register(BlockEntities.VAULT_MANAGER_ENTITY,
                                VaultManagerEntityRenderer::new);

                BlockEntityRendererRegistry.register(BlockEntities.VAULT_LOCK_ENTITY,
                                VaultLockEntityRenderer::new);

                BlockRenderLayerMap.INSTANCE.putBlock(Blocks.CONSUL_PROXY_BLOCK,
                                RenderLayer.getTranslucent());

                BlockRenderLayerMap.INSTANCE.putBlock(Blocks.CONSUL_CONTROLLER_BLOCK,
                                RenderLayer.getTranslucent());

                BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NOMAD_WIRES_BLOCK,
                                RenderLayer.getTranslucent());

                BlockRenderLayerMap.INSTANCE.putBlock(Blocks.NOMAD_SPIN_BLOCK,
                                RenderLayer.getTranslucent());

                BlockRenderLayerMap.INSTANCE.putBlock(Blocks.WAYPOINT_PIPELINE_BLOCK,
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
        }
}
