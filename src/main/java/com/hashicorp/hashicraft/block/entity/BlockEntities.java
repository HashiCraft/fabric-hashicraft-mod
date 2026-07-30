package com.hashicorp.hashicraft.block.entity;

import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import java.util.Set;

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
    NOMAD_SERVER_ENTITY = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        Mod.identifier("nomad_server"),
        new BlockEntityType<>(NomadServerEntity::new, Set.of(ModBlocks.NOMAD_SERVER_BLOCK)));

    NOMAD_DISPENSER_ENTITY = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        Mod.identifier("nomad_dispenser"),
        new BlockEntityType<>(NomadDispenserEntity::new, Set.of(ModBlocks.NOMAD_DISPENSER_BLOCK)));

    VAULT_DISPENSER_ENTITY = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        Mod.identifier("vault_dispenser"),
        new BlockEntityType<>(VaultDispenserEntity::new, Set.of(ModBlocks.VAULT_DISPENSER_BLOCK)));

    VAULT_LOCK_ENTITY = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        Mod.identifier("vault_lock"),
        new BlockEntityType<>(VaultLockEntity::new, Set.of(ModBlocks.VAULT_LOCK_BLOCK)));

    BOUNDARY_LOCK_ENTITY = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        Mod.identifier("boundary_lock"),
        new BlockEntityType<>(BoundaryLockEntity::new, Set.of(ModBlocks.BOUNDARY_LOCK_BLOCK)));

    CONSUL_RELEASER_ENTITY = Registry.register(
        BuiltInRegistries.BLOCK_ENTITY_TYPE,
        Mod.identifier("consul_releaser"),
        new BlockEntityType<>(ConsulReleaserEntity::new, Set.of(ModBlocks.CONSUL_RELEASER_BLOCK)));
  }
}
