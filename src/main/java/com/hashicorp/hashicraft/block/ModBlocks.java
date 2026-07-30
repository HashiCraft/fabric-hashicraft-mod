package com.hashicorp.hashicraft.block;

import com.hashicorp.hashicraft.Mod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import java.util.function.Function;

public class ModBlocks {
  // Consul
  public static final Block CONSUL_RELEASER_BLOCK = register("consul_releaser",
      settings -> new ConsulReleaserBlock(settings),
      BlockBehaviour.Properties.of().strength(4.0f).noOcclusion());

  // Nomad
  public static final Block NOMAD_SERVER_BLOCK = register("nomad_server",
      settings -> new NomadServerBlock(settings),
      BlockBehaviour.Properties.of().strength(4.0f).noOcclusion());

  public static final Block NOMAD_DISPENSER_BLOCK = register("nomad_dispenser",
      settings -> new NomadDispenserBlock(settings),
      BlockBehaviour.Properties.of().strength(4.0f).noOcclusion());

  // Vault
  public static final Block VAULT_DISPENSER_BLOCK = register("vault_dispenser",
      settings -> new VaultDispenserBlock(settings),
      BlockBehaviour.Properties.of().strength(4.0f).noOcclusion());

  public static final Block VAULT_LOCK_BLOCK = register("vault_lock",
      settings -> new VaultLockBlock(settings),
      BlockBehaviour.Properties.of().strength(4.0f).noOcclusion());

  // Boundary
  public static final Block BOUNDARY_LOCK_BLOCK = register("boundary_lock",
      settings -> new BoundaryLockBlock(settings),
      BlockBehaviour.Properties.of().strength(4.0f).noOcclusion());

  private static Block register(String path,
                                Function<BlockBehaviour.Properties, Block> factory,
                                BlockBehaviour.Properties settings) {
    Identifier identifier = Mod.identifier(path);
    ResourceKey<Block> registryKey = ResourceKey.create(Registries.BLOCK, identifier);
    return Registry.register(BuiltInRegistries.BLOCK, registryKey, factory.apply(settings.setId(registryKey)));
  }

  public static void register() {
    // Blocks are already registered during static initialization
  }
}