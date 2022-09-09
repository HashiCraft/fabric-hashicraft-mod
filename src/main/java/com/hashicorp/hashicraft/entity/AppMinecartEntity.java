package com.hashicorp.hashicraft.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AppMinecartEntity extends MinecartEntity {
  // private Text allocation;
  // private Text application;
  // private Text version;

  // private static final TrackedData<Optional<Text>> ALLOCATION =
  // DataTracker.registerData(AppMinecartEntity.class,
  // TrackedDataHandlerRegistry.OPTIONAL_TEXT_COMPONENT);
  // private static final TrackedData<Optional<Text>> APPLICATION =
  // DataTracker.registerData(AppMinecartEntity.class,
  // TrackedDataHandlerRegistry.OPTIONAL_TEXT_COMPONENT);
  // private static final TrackedData<Optional<Text>> VERSION =
  // DataTracker.registerData(AppMinecartEntity.class,
  // TrackedDataHandlerRegistry.OPTIONAL_TEXT_COMPONENT);

  public AppMinecartEntity(EntityType<?> type, World world) {
    super(type, world);
  }

  @Override
  public Type getMinecartType() {
    return null;
  }

  // public void setAllocation(Text allocation) {
  // this.dataTracker.set(ALLOCATION, Optional.ofNullable(allocation));
  // }

  // public Text getAllocation() {
  // return this.dataTracker.get(ALLOCATION).orElse(null);
  // }

  // public void setApplication(Text application) {
  // this.dataTracker.set(APPLICATION, Optional.ofNullable(application));
  // }

  // public Text getApplication() {
  // return this.dataTracker.get(APPLICATION).orElse(null);
  // }

  // public void setVersion(Text version) {
  // this.dataTracker.set(VERSION, Optional.ofNullable(version));
  // }

  // public Text getVersion() {
  // return this.dataTracker.get(VERSION).orElse(null);
  // }

  @Override
  protected void readCustomDataFromNbt(NbtCompound nbt) {
    super.readCustomDataFromNbt(nbt);
  }

  @Override
  protected void writeCustomDataToNbt(NbtCompound nbt) {
    super.writeCustomDataToNbt(nbt);
  }

  @Override
  public Packet<?> createSpawnPacket() {
    return new EntitySpawnS2CPacket(this);
  }

  @Override
  protected Item getItem() {
    return null;
  }

  @Override
  public void dropItems(DamageSource damageSource) {
    this.kill();
  }

  @Override
  protected void moveOnRail(BlockPos pos, BlockState state) {
    super.moveOnRail(pos, state);
  }

  @Override
  public void tick() {
    super.tick();
  }

  @Override
  public boolean damage(DamageSource source, float amount) {
    // this.discard();
    // return true;
    return false;
  }
}
