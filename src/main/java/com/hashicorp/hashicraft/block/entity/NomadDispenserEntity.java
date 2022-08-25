package com.hashicorp.hashicraft.block.entity;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.hashicorp.hashicraft.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.*;

public class NomadDispenserEntity extends StatefulBlockEntity {
  public static final String vaultAddress = System.getenv().getOrDefault("VAULT_ADDR", "http://localhost:8200");
  public static final String vaultToken = System.getenv().getOrDefault("VAULT_TOKEN", "root");

  @Syncable
  private String name = "fake-service";

  @Syncable
  private String version = "v0.24.2";

  @Syncable
  private String color = "black";

  public NomadDispenserEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.NOMAD_DISPENSER_ENTITY, pos, state, null);
  }

  public NomadDispenserEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.NOMAD_DISPENSER_ENTITY, pos, state, parent);
  }

  public void dispense(World world, BlockPointerImpl pointer, ItemStack stack, int offset, Direction side) {
    double x = pointer.getX() + 0.7D * (double) side.getOffsetX();
    double y = pointer.getY() + 0.7D * (double) side.getOffsetY();
    double z = pointer.getZ() + 0.7D * (double) side.getOffsetZ();

    if (side.getAxis() == Direction.Axis.Y) {
      y -= 0.125D;
    } else {
      y -= 0.15625D;
    }

    ItemEntity entity = new ItemEntity(world, x, y, z, stack);
    double g = world.random.nextDouble() * 0.1D + 0.2D;
    entity.setVelocity(
            world.random.nextGaussian() * 0.007499999832361937D * (double) offset + (double) side.getOffsetX() * g,
            world.random.nextGaussian() * 0.007499999832361937D * (double) offset + 0.20000000298023224D,
            world.random.nextGaussian() * 0.007499999832361937D * (double) offset + (double) side.getOffsetZ() * g);
    world.spawnEntity(entity);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    this.markForUpdate();
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
    this.markForUpdate();
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
    this.markForUpdate();
  }

  public ItemStack getApplication(String owner) {
    ItemStack application = new ItemStack(ModItems.APPLICATION_ITEM);

    NbtCompound identity = application.getOrCreateNbt();
    identity.putString("name", this.getName());
    identity.putString("version", this.getVersion());
    identity.putString("owner", owner);
    application.setNbt(identity);

    return application;
  }

  public ItemStack getDye() {
    return new ItemStack(switch (this.getColor()) {
      case "white" -> Items.WHITE_DYE;
      case "orange" -> Items.ORANGE_DYE;
      case "magenta" -> Items.MAGENTA_DYE;
      case "light blue" -> Items.LIGHT_BLUE_DYE;
      case "yellow" -> Items.YELLOW_DYE;
      case "lime" -> Items.LIME_DYE;
      case "pink" -> Items.PINK_DYE;
      case "gray" -> Items.GRAY_DYE;
      case "light gray" -> Items.LIGHT_GRAY_DYE;
      case "cyan" -> Items.CYAN_DYE;
      case "purple" -> Items.PURPLE_DYE;
      case "blue" -> Items.BLUE_DYE;
      case "brown" -> Items.BROWN_DYE;
      case "green" -> Items.GREEN_DYE;
      case "red" -> Items.RED_DYE;
      default -> Items.BLACK_DYE;
    });
  }

  public ItemStack getMinecart() {
    return new ItemStack(Items.MINECART);
  }
}
