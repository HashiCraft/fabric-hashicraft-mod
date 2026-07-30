package com.hashicorp.hashicraft;

import com.hashicorp.hashicraft.block.ModBlocks;
import com.hashicorp.hashicraft.block.entity.BlockEntities;
import com.hashicorp.hashicraft.block.entity.ConsulReleaserRenderer;
import com.hashicorp.hashicraft.block.entity.VaultDispenserRenderer;
import com.hashicorp.hashicraft.entity.ModEntities;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.hashicraft.ui.*;
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

    // TODO: Fabric API Event - Uncomment after updating Fabric API
    /*
    NomadDispenserClicked.EVENT.register((block, callback) -> {
      NomadDispenserGui gui = new NomadDispenserGui(block, callback);
      NomadDispenserScreen screen = new NomadDispenserScreen(gui);
      Minecraft.getInstance().gui.setScreen(screen);
      return InteractionResult.PASS;
    });
    */

    // Nomad Server
    // TODO: Fabric API Event - Uncomment after updating Fabric API
    /*
    NomadServerClicked.EVENT.register((block, callback) -> {
      NomadServerGui gui = new NomadServerGui(block, callback);
      NomadServerScreen screen = new NomadServerScreen(gui);
      Minecraft.getInstance().gui.setScreen(screen);
      return InteractionResult.PASS;
    });
    */

    // Consul Releaser
    BlockEntityRenderers.register(BlockEntities.CONSUL_RELEASER_ENTITY,
        ConsulReleaserRenderer::new);

    // Vault Dispenser
    // TODO: Update Fabric API - BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.VAULT_DISPENSER_BLOCK, RenderType.cutoutMipped());

    BlockEntityRenderers.register(BlockEntities.VAULT_DISPENSER_ENTITY,
        VaultDispenserRenderer::new);

    // TODO: Fabric API Event - Uncomment after updating Fabric API
    /*
    VaultDispenserClicked.EVENT.register((block, callback) -> {
      VaultDispenserGui gui = new VaultDispenserGui(block, callback);
      VaultDispenserScreen screen = new VaultDispenserScreen(gui);
      Minecraft.getInstance().gui.setScreen(screen);
      return InteractionResult.PASS;
    });
    */

    // Vault Lock
    // TODO: Fabric API Event - Uncomment after updating Fabric API
    /*
    VaultLockClicked.EVENT.register((block, callback) -> {
      VaultLockGui gui = new VaultLockGui(block, callback);
      VaultLockScreen screen = new VaultLockScreen(gui);
      Minecraft.getInstance().gui.setScreen(screen);
      return InteractionResult.PASS;
    });
    */

    // Boundary lock
    // TODO: Fabric API Event - Uncomment after updating Fabric API
    /*
    BoundaryLockClicked.EVENT.register((block, callback) -> {
      BoundaryLockGui gui = new BoundaryLockGui(block, callback);
      BoundaryLockScreen screen = new BoundaryLockScreen(gui);
      Minecraft.getInstance().gui.setScreen(screen);
      return InteractionResult.PASS;
    });
    */

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