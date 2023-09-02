package com.hashicorp.hashicraft.entity;

import java.util.Optional;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AppMinecartEntity extends MinecartEntity {

  private static final TrackedData<Optional<Text>> ALLOCATION;
  private static final TrackedData<Optional<Text>> APPLICATION;
  private static final TrackedData<Optional<Text>> VERSION;

  static {
    ALLOCATION = DataTracker.registerData(
        AppMinecartEntity.class, TrackedDataHandlerRegistry.OPTIONAL_TEXT_COMPONENT);
    APPLICATION = DataTracker.registerData(
        AppMinecartEntity.class, TrackedDataHandlerRegistry.OPTIONAL_TEXT_COMPONENT);
    VERSION = DataTracker.registerData(
        AppMinecartEntity.class, TrackedDataHandlerRegistry.OPTIONAL_TEXT_COMPONENT);
  }

  public AppMinecartEntity(EntityType<?> type, World world) {
    super(type, world);
    this.dataTracker.startTracking(ALLOCATION, Optional.empty());
    this.dataTracker.startTracking(APPLICATION, Optional.empty());
    this.dataTracker.startTracking(VERSION, Optional.empty());
  }

  public void setAllocationID(@Nullable Text allocationID) {
    this.dataTracker.set(ALLOCATION, Optional.ofNullable(allocationID));
  }

  @Nullable
  public Text getAllocationID() {
    return (Text) ((Optional<Text>) this.dataTracker.get(ALLOCATION)).orElse(Text.empty());
  }

  public void setApplication(@Nullable Text application) {
    this.dataTracker.set(APPLICATION, Optional.ofNullable(application));
  }

  @Nullable
  public Text getApplication() {
    return (Text) ((Optional<Text>) this.dataTracker.get(APPLICATION)).orElse(Text.empty());
  }

  public void setVersion(@Nullable Text version) {
    this.dataTracker.set(VERSION, Optional.ofNullable(version));
  }

  @Nullable
  public Text getVersion() {
    return (Text) ((Optional<Text>) this.dataTracker.get(VERSION)).orElse(Text.empty());
  }

  @Override
  public Type getMinecartType() {
    return null;
  }

  @Override
  protected void readCustomDataFromNbt(NbtCompound nbt) {
    super.readCustomDataFromNbt(nbt);
  }

  @Override
  protected void writeCustomDataToNbt(NbtCompound nbt) {
    super.writeCustomDataToNbt(nbt);
  }

  @Override
  public Packet<ClientPlayPacketListener> createSpawnPacket() {
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
    return false;
  }

  @Override
  public boolean isCollidable() {
    return false;
  }

  @Override
  public boolean isPushable() {
    return false;
  }

  @Override
  public boolean collidesWith(Entity other) {
    return false;
  }
}
