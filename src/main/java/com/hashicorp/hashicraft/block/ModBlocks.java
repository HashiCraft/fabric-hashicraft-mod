package com.hashicorp.hashicraft.block;

import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.item.ItemGroups;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class ModBlocks {
        // Consul
        public static final Block CONSUL_RELEASER_BLOCK = registerBlock("consul_releaser",
                        new ConsulReleaserBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()),
                        ItemGroups.HASHICRAFT);

        // Nomad
        public static final Block NOMAD_SERVER_BLOCK = registerBlock("nomad_server",
                        new NomadServerBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()),
                        ItemGroups.HASHICRAFT);

        public static final Block NOMAD_DISPENSER_BLOCK = registerBlock("nomad_dispenser",
                        new NomadDispenserBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()),
                        ItemGroups.HASHICRAFT);

        // Vault
        public static final Block VAULT_DISPENSER_BLOCK = registerBlock("vault_dispenser",
                        new VaultDispenserBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()),
                        ItemGroups.HASHICRAFT);

        public static final Block VAULT_LOCK_BLOCK = registerBlock("vault_lock",
                        new VaultLockBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()), ItemGroups.HASHICRAFT);

        // Boundary
        public static final Block BOUNDARY_LOCK_BLOCK = registerBlock("boundary_lock",
                        new BoundaryLockBlock(FabricBlockSettings.of(Material.METAL).nonOpaque()),
                        ItemGroups.HASHICRAFT);

        private static Block registerBlock(String name, Block block, ItemGroup group) {
                registerBlockItem(name, block, group);

                return Registry.register(Registry.BLOCK, Mod.identifier(name), block);
        }

        private static Item registerBlockItem(String name, Block block, ItemGroup group) {
                return Registry.register(Registry.ITEM, Mod.identifier(name),
                                new BlockItem(block, new FabricItemSettings().group(group)));
        }

        public static void register() {
        }
}