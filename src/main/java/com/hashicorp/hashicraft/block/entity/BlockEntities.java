package com.hashicorp.hashicraft.block.entity;

import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

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
        NOMAD_SERVER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Mod.identifier("nomad_server"),
                FabricBlockEntityTypeBuilder
                        .create(NomadServerEntity::new, ModBlocks.NOMAD_SERVER_BLOCK)
                        .build(null));

        NOMAD_DISPENSER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                Mod.identifier("nomad_dispenser"),
                FabricBlockEntityTypeBuilder
                        .create(NomadDispenserEntity::new, ModBlocks.NOMAD_DISPENSER_BLOCK)
                        .build(null));

        // Vault
        VAULT_LOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Mod.identifier("vault_lock"),
                FabricBlockEntityTypeBuilder.create(VaultLockEntity::new, ModBlocks.VAULT_LOCK_BLOCK)
                        .build(null));

        VAULT_DISPENSER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                Mod.identifier("vault_dispenser"),
                FabricBlockEntityTypeBuilder
                        .create(VaultDispenserEntity::new, ModBlocks.VAULT_DISPENSER_BLOCK)
                        .build(null));

        // Boundary
        BOUNDARY_LOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Mod.identifier("boundary_lock"),
                FabricBlockEntityTypeBuilder
                        .create(BoundaryLockEntity::new, ModBlocks.BOUNDARY_LOCK_BLOCK)
                        .build(null));

        // Consul
        CONSUL_RELEASER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                Mod.identifier("consul_releaser"),
                FabricBlockEntityTypeBuilder
                        .create(ConsulReleaserEntity::new, ModBlocks.CONSUL_RELEASER_BLOCK)
                        .build(null));
    }
}