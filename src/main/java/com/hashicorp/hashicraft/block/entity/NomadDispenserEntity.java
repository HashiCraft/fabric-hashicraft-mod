package com.hashicorp.hashicraft.block.entity;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.google.common.collect.Lists;
import com.hashicorp.hashicraft.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hashicorp.hashicraft.item.Dyes.COLORS;
import static com.hashicorp.hashicraft.item.Dyes.DEFAULT_COLOR;

public class NomadDispenserEntity extends StatefulBlockEntity {
  @Syncable
  private String name = "fake-service";

  @Syncable
  private String color = "blue";

  @Syncable
  private String version = color;

  @Syncable
  private List<String> colors = Lists.newArrayList(color);

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

  public String getColors() {
    return String.join(",", this.colors);
  }

  public void setColorAndVersion(String color, String version) {
    this.color = color;
    this.version = version;
    this.markForUpdate();
  }

  public void setColors(String colorText) {
    this.colors = Stream
            .of(colorText.split(",", -1))
            .collect(Collectors.toList());
    setColorAndVersion(this.colors.get(0), this.colors.get(0));
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
    String dyeColor = this.color.replace(' ', '_') + "_dye";
    boolean isColorPresent = COLORS.containsKey(dyeColor);
    if (isColorPresent) {
      return new ItemStack(COLORS.get(dyeColor));
    }
    return new ItemStack(DEFAULT_COLOR);
  }

  public ItemStack getMinecart() {
    return new ItemStack(Items.MINECART);
  }

  public void changeColors() {
    String color = this.colors.get(0);
    int i = this.colors.indexOf(this.color);
    if (i+1 != this.colors.size()) {
      color = this.colors.get(i + 1);
    }
    setColorAndVersion(color, color);
  }
}
