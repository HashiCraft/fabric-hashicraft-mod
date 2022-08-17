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
        // Generic
        public static final Block COMPUTER_BLOCK = registerBlock("computer",
                        new Computer(FabricBlockSettings.of(Material.METAL).nonOpaque()), ItemGroups.HASHICRAFT);

        public static final Block PHONE_BLOCK = registerBlock("phone",
                        new Phone(FabricBlockSettings.of(Material.METAL).nonOpaque()),
                        ItemGroups.HASHICRAFT);

        // Consul
        public static final Block CONSUL_PROXY_BLOCK = registerBlock("consul_proxy",
                        new ConsulProxy(FabricBlockSettings.of(Material.METAL).nonOpaque()),
                        ItemGroups.HASHICRAFT);

        public static final Block CONSUL_CONTROLLER_BLOCK = registerBlock("consul_controller",
                        new ConsulController(FabricBlockSettings.of(Material.METAL).nonOpaque()),
                        ItemGroups.HASHICRAFT);

        public static final Block CONSUL_RELEASER_BLOCK = registerBlock("consul_releaser",
                        new ConsulReleaser(FabricBlockSettings.of(Material.METAL).nonOpaque()),
                        ItemGroups.HASHICRAFT);

        public static final Block CONSUL_RELEASE_BLOCK = registerBlock("consul_release",
                        new ConsulRelease(FabricBlockSettings.of(Material.METAL).nonOpaque()),
                        ItemGroups.HASHICRAFT);

        // Nomad
        public static final Block NOMAD_SERVER_BLOCK = registerBlock("nomad_server",
                        new NomadServer(FabricBlockSettings.of(Material.METAL).nonOpaque()), ItemGroups.HASHICRAFT);

        public static final Block NOMAD_CLIENT_BLOCK = registerBlock("nomad_client",
                        new NomadClient(FabricBlockSettings.of(Material.METAL).nonOpaque()), ItemGroups.HASHICRAFT);

        public static final Block NOMAD_ALLOC_BLOCK = registerBlock("nomad_alloc",
                        new NomadAlloc(FabricBlockSettings.of(Material.METAL).nonOpaque()), ItemGroups.HASHICRAFT);

        public static final Block NOMAD_WIRES_BLOCK = registerBlock("nomad_wires",
                        new NomadWires(FabricBlockSettings.of(Material.METAL).nonOpaque()), ItemGroups.HASHICRAFT);

        public static final Block NOMAD_SPIN_BLOCK = registerBlock("nomad_spin",
                        new NomadSpin(FabricBlockSettings.of(Material.METAL).nonOpaque()), ItemGroups.HASHICRAFT);

        public static final Block NOMAD_WHISKERS_BLOCK = registerBlock("nomad_whiskers",
                        new NomadWhiskers(FabricBlockSettings.of(Material.METAL).nonOpaque()), ItemGroups.HASHICRAFT);

        // Dispensers for two versions of application
        public static final Block NOMAD_DISPENSER_BLOCK = registerBlock("nomad_dispenser",
                new NomadDispenser(FabricBlockSettings.of(Material.METAL).nonOpaque(), ""),
                ItemGroups.HASHICRAFT);

        public static final Block APPLICATION_V2_DISPENSER_BLOCK = registerBlock("application_v2_dispenser",
                new NomadDispenser(FabricBlockSettings.of(Material.METAL).nonOpaque(), "v2"),
                ItemGroups.HASHICRAFT);

        public static final Block APPLICATION_V3_DISPENSER_BLOCK = registerBlock("application_v3_dispenser",
                new NomadDispenser(FabricBlockSettings.of(Material.METAL).nonOpaque(), "v3"),
                ItemGroups.HASHICRAFT);

        // Vault
        public static final Block VAULT_DISPENSER_BLOCK = registerBlock("vault_dispenser",
                        new VaultDispenser(FabricBlockSettings.of(Material.METAL).nonOpaque()), ItemGroups.HASHICRAFT);

        public static final Block VAULT_MANAGER_BLOCK = registerBlock("vault_manager",
                        new VaultManager(FabricBlockSettings.of(Material.METAL).nonOpaque()), ItemGroups.HASHICRAFT);

        public static final Block VAULT_LOCK_BLOCK = registerBlock("vault_lock",
                        new VaultLock(FabricBlockSettings.of(Material.METAL).nonOpaque()), ItemGroups.HASHICRAFT);

        // Waypoint
        public static final Block WAYPOINT_SERVER_BLOCK = registerBlock("waypoint_server",
                        new WaypointServer(FabricBlockSettings.of(Material.METAL).nonOpaque()), ItemGroups.HASHICRAFT);

        public static final Block WAYPOINT_STEP_BLOCK = registerBlock("waypoint_step",
                        new WaypointStep(FabricBlockSettings.of(Material.METAL).nonOpaque()),
                        ItemGroups.HASHICRAFT);

        public static final Block WAYPOINT_PIPELINE_BLOCK = registerBlock("waypoint_pipeline",
                        new WaypointPipeline(FabricBlockSettings.of(Material.METAL).nonOpaque()),
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