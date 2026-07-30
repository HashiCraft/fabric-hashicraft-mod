package com.hashicorp.hashicraft;

import com.hashicorp.hashicraft.block.ModBlocks;
import com.hashicorp.hashicraft.block.entity.BlockEntities;
import com.hashicorp.hashicraft.block.entity.ConsulReleaserRenderer;
import com.hashicorp.hashicraft.block.entity.VaultDispenserRenderer;
import com.hashicorp.hashicraft.entity.ModEntities;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.hashicraft.ui.*;
import com.hashicorp.hashicraft.ui.event.BoundaryLockClicked;
import com.hashicorp.hashicraft.ui.event.NomadDispenserClicked;
import com.hashicorp.hashicraft.ui.event.NomadServerClicked;
import com.hashicorp.hashicraft.ui.event.VaultDispenserClicked;
import com.hashicorp.hashicraft.ui.event.VaultLockClicked;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
// TODO: Fabric API - import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.Minecraft;

// TODO: Fabric API - import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.InteractionResult;

@Environment(EnvType.CLIENT)
public class ModClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    // TODO: Fabric API - Uncomment after updating Fabric API
    // EntityModelLayerRegistry.registerModelLayer(AppMinecartEntityModel.LAYER_LOCATION, AppMinecartEntityModel::createBodyLayer);

    ModItems.register();
    ModBlocks.register();

    // Nomad Dispenser
    // TODO: Update Fabric API - BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NOMAD_DISPENSER_BLOCK, RenderType.translucent());

    NomadDispenserClicked.EVENT.register((block, callback) -> {
      NomadDispenserGui gui = new NomadDispenserGui(block, callback);
      NomadDispenserScreen screen = new NomadDispenserScreen(gui);
      Minecraft.getInstance().setScreenAndShow(screen);
      return InteractionResult.SUCCESS;
    });

    // Nomad Server
    NomadServerClicked.EVENT.register((block, callback) -> {
      NomadServerGui gui = new NomadServerGui(block, callback);
      NomadServerScreen screen = new NomadServerScreen(gui);
      Minecraft.getInstance().setScreenAndShow(screen);
      return InteractionResult.SUCCESS;
    });

    // Consul Releaser
    BlockEntityRenderers.register(BlockEntities.CONSUL_RELEASER_ENTITY,
        ConsulReleaserRenderer::new);

    // Vault Dispenser
    // TODO: Update Fabric API - BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.VAULT_DISPENSER_BLOCK, RenderType.cutoutMipped());

    BlockEntityRenderers.register(BlockEntities.VAULT_DISPENSER_ENTITY,
        VaultDispenserRenderer::new);

    VaultDispenserClicked.EVENT.register((block, callback) -> {
      VaultDispenserGui gui = new VaultDispenserGui(block, callback);
      VaultDispenserScreen screen = new VaultDispenserScreen(gui);
      Minecraft.getInstance().setScreenAndShow(screen);
      return InteractionResult.SUCCESS;
    });

    // Vault Lock
    VaultLockClicked.EVENT.register((block, callback) -> {
      VaultLockGui gui = new VaultLockGui(block, callback);
      VaultLockScreen screen = new VaultLockScreen(gui);
      Minecraft.getInstance().setScreenAndShow(screen);
      return InteractionResult.SUCCESS;
    });

    // Boundary lock
    BoundaryLockClicked.EVENT.register((block, callback) -> {
      BoundaryLockGui gui = new BoundaryLockGui(block, callback);
      BoundaryLockScreen screen = new BoundaryLockScreen(gui);
      Minecraft.getInstance().setScreenAndShow(screen);
      return InteractionResult.SUCCESS;
    });

    ModEntities.register();

    // App Minecart
    // TODO: Fabric API - Uncomment after updating Fabric API
    /*
    EntityRendererRegistry.register(ModEntities.APP_MINECART, (context) -> {
      return new AppMinecartEntityRenderer(context);
    });
    */
  }
}