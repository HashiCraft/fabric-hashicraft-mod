package com.hashicorp.hashicraft.block.entity;

import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.block.ModBlocks;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class BlockEntities {
        // Nomad
        public static BlockEntityType<NomadServerEntity> NOMAD_SERVER_ENTITY;
        public static BlockEntityType<NomadSpinEntity> NOMAD_SPIN_ENTITY;
        public static BlockEntityType<NomadWhiskersEntity> NOMAD_WHISKERS_ENTITY;
        public static BlockEntityType<NomadAllocEntity> NOMAD_ALLOC_ENTITY;

        // Vault
        public static BlockEntityType<VaultLockEntity> VAULT_LOCK_ENTITY;
        public static BlockEntityType<VaultDispenserEntity> VAULT_DISPENSER_ENTITY;
        public static BlockEntityType<VaultManagerEntity> VAULT_MANAGER_ENTITY;

        // Consul
        public static BlockEntityType<ConsulControllerEntity> CONSUL_CONTROLLER_ENTITY;
        public static BlockEntityType<ConsulReleaserEntity> CONSUL_RELEASER_ENTITY;
        public static BlockEntityType<ConsulReleaseEntity> CONSUL_RELEASE_ENTITY;

        public static void register() {
                // Nomad
                NOMAD_SERVER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Mod.identifier("nomad_server"),
                                FabricBlockEntityTypeBuilder
                                                .create(NomadServerEntity::new, ModBlocks.NOMAD_SERVER_BLOCK)
                                                .build(null));

                NOMAD_SPIN_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Mod.identifier("nomad_spin"),
                                FabricBlockEntityTypeBuilder.create(NomadSpinEntity::new, ModBlocks.NOMAD_SPIN_BLOCK)
                                                .build(null));

                NOMAD_WHISKERS_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Mod.identifier("nomad_whiskers"),
                                FabricBlockEntityTypeBuilder
                                                .create(NomadWhiskersEntity::new, ModBlocks.NOMAD_WHISKERS_BLOCK)
                                                .build(null));

                NOMAD_ALLOC_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, Mod.identifier("nomad_alloc"),
                                FabricBlockEntityTypeBuilder
                                                .create(NomadAllocEntity::new, ModBlocks.NOMAD_ALLOC_BLOCK)
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

                VAULT_MANAGER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                                Mod.identifier("vault_manager"),
                                FabricBlockEntityTypeBuilder
                                                .create(VaultManagerEntity::new, ModBlocks.VAULT_MANAGER_BLOCK)
                                                .build(null));

                // Consul
                CONSUL_RELEASER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                                Mod.identifier("consul_releaser"),
                                FabricBlockEntityTypeBuilder
                                                .create(ConsulReleaserEntity::new, ModBlocks.CONSUL_RELEASER_BLOCK)
                                                .build(null));

                CONSUL_RELEASE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                                Mod.identifier("consul_release"),
                                FabricBlockEntityTypeBuilder
                                                .create(ConsulReleaseEntity::new, ModBlocks.CONSUL_RELEASE_BLOCK)
                                                .build(null));

                CONSUL_CONTROLLER_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                                Mod.identifier("consul_controller"),
                                FabricBlockEntityTypeBuilder
                                                .create(ConsulControllerEntity::new, ModBlocks.CONSUL_CONTROLLER_BLOCK)
                                                .build(null));
        }
}