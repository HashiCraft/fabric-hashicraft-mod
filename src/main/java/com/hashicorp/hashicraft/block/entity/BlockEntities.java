package com.hashicorp.hashicraft.block.entity;

import com.hashicorp.hashicraft.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;

public class BlockEntities {
  // Nomad

  public static BlockEntityType<NomadServerEntity> NOMAD_SERVER_ENTITY;
  public static BlockEntityType<NomadDispenserEntity> NOMAD_DISPENSER_ENTITY;

  // Vault
  public static BlockEntityType<VaultLockEntity> VAULT_LOCK_ENTITY;
  public static BlockEntityType<VaultDispenserEntity> VAULT_DISPENSER_ENTITY;

  // Boundary
  public static BlockEntityType<BoundaryLockEntity> BOUNDARY_LOCK_ENTITY;

  // Consul
  public static BlockEntityType<ConsulReleaserEntity> CONSUL_RELEASER_ENTITY;

  public static void register() {
    // Nomad
    NOMAD_SERVER_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, ModBlocks.NOMAD_SERVER_ID,
        FabricBlockEntityTypeBuilder.create(NomadServerEntity::new, ModBlocks.NOMAD_SERVER_BLOCK).build());

    NOMAD_DISPENSER_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, ModBlocks.NOMAD_DISPENSER_ID,
        FabricBlockEntityTypeBuilder.create(NomadDispenserEntity::new, ModBlocks.NOMAD_DISPENSER_BLOCK).build());

    VAULT_DISPENSER_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, ModBlocks.VAULT_DISPENSER_ID,
        FabricBlockEntityTypeBuilder.create(VaultDispenserEntity::new, ModBlocks.VAULT_DISPENSER_BLOCK).build());

    VAULT_LOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, ModBlocks.VAULT_LOCK_ID,
        FabricBlockEntityTypeBuilder.create(VaultLockEntity::new, ModBlocks.VAULT_LOCK_BLOCK).build());

    BOUNDARY_LOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, ModBlocks.BOUNDARY_LOCK_ID,
        FabricBlockEntityTypeBuilder.create(BoundaryLockEntity::new, ModBlocks.BOUNDARY_LOCK_BLOCK).build());

    CONSUL_RELEASER_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, ModBlocks.CONSUL_RELEASER_ID,
        FabricBlockEntityTypeBuilder.create(ConsulReleaserEntity::new, ModBlocks.CONSUL_RELEASER_BLOCK).build());
  }
}