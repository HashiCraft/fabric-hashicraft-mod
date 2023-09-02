package com.hashicorp.hashicraft.block;

import com.hashicorp.hashicraft.Mod;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModBlocks {
  // Consul
  public static final Identifier CONSUL_RELEASER_ID = Mod.identifier("consul_releaser");
  public static final Block CONSUL_RELEASER_BLOCK = new ConsulReleaserBlock(
      FabricBlockSettings.create().strength(4.0f).nonOpaque().solid());

  // Nomad
  public static final Identifier NOMAD_SERVER_ID = Mod.identifier("nomad_server");
  public static final Block NOMAD_SERVER_BLOCK = new NomadServerBlock(
      FabricBlockSettings.create().strength(4.0f).nonOpaque().solid());

  public static final Identifier NOMAD_DISPENSER_ID = Mod.identifier("nomad_dispenser");
  public static final Block NOMAD_DISPENSER_BLOCK = new NomadDispenserBlock(
      FabricBlockSettings.create().strength(4.0f).nonOpaque().solid());

  // Vault
  public static final Identifier VAULT_DISPENSER_ID = Mod.identifier("vault_dispenser");
  public static final Block VAULT_DISPENSER_BLOCK = new VaultDispenserBlock(
      FabricBlockSettings.create().strength(4.0f).nonOpaque().solid());

  public static final Identifier VAULT_LOCK_ID = Mod.identifier("vault_lock");
  public static final Block VAULT_LOCK_BLOCK = new VaultLockBlock(
      FabricBlockSettings.create().strength(4.0f).nonOpaque().solid());

  // Boundary
  public static final Identifier BOUNDARY_LOCK_ID = Mod.identifier("boundary_lock");
  public static final Block BOUNDARY_LOCK_BLOCK = new BoundaryLockBlock(
      FabricBlockSettings.create().strength(4.0f).nonOpaque().solid());

  public static void register() {
    Registry.register(Registries.BLOCK, CONSUL_RELEASER_ID, CONSUL_RELEASER_BLOCK);

    Registry.register(Registries.BLOCK, NOMAD_SERVER_ID, NOMAD_SERVER_BLOCK);
    Registry.register(Registries.BLOCK, NOMAD_DISPENSER_ID, NOMAD_DISPENSER_BLOCK);

    Registry.register(Registries.BLOCK, VAULT_DISPENSER_ID, VAULT_DISPENSER_BLOCK);
    Registry.register(Registries.BLOCK, VAULT_LOCK_ID, VAULT_LOCK_BLOCK);

    Registry.register(Registries.BLOCK, BOUNDARY_LOCK_ID, BOUNDARY_LOCK_BLOCK);
  }
}