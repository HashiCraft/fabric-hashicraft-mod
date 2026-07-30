package com.hashicorp.hashicraft.entity;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.vehicle.minecart.Minecart;
import net.minecraft.world.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
public class AppMinecartEntity extends Minecart {

  private static final EntityDataAccessor<Optional<Component>> ALLOCATION;
  private static final EntityDataAccessor<Optional<Component>> APPLICATION;
  private static final EntityDataAccessor<Optional<Component>> VERSION;

  static {
    ALLOCATION = SynchedEntityData.defineId(
        AppMinecartEntity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
    APPLICATION = SynchedEntityData.defineId(
        AppMinecartEntity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
    VERSION = SynchedEntityData.defineId(
        AppMinecartEntity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
  }

  public AppMinecartEntity(EntityType<?> type, Level level) {
    super(type, level);
  }

  @Override
  protected void defineSynchedData(SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    builder.define(ALLOCATION, Optional.empty());
    builder.define(APPLICATION, Optional.empty());
    builder.define(VERSION, Optional.empty());
  }

  public void setAllocationID(@Nullable Component allocationID) {
    this.entityData.set(ALLOCATION, Optional.ofNullable(allocationID));
  }

  @Nullable
  public Component getAllocationID() {
    return (Component) ((Optional<Component>) this.entityData.get(ALLOCATION)).orElse(Component.empty());
  }

  public void setApplication(@Nullable Component application) {
    this.entityData.set(APPLICATION, Optional.ofNullable(application));
  }

  @Nullable
  public Component getApplication() {
    return (Component) ((Optional<Component>) this.entityData.get(APPLICATION)).orElse(Component.empty());
  }

  public void setVersion(@Nullable Component version) {
    this.entityData.set(VERSION, Optional.ofNullable(version));
  }

  @Nullable
  public Component getVersion() {
    return (Component) ((Optional<Component>) this.entityData.get(VERSION)).orElse(Component.empty());
  }

  @Override
  protected void readAdditionalSaveData(net.minecraft.world.level.storage.ValueInput input) {
    super.readAdditionalSaveData(input);
  }

  @Override
  protected void addAdditionalSaveData(net.minecraft.world.level.storage.ValueOutput output) {
    super.addAdditionalSaveData(output);
  }

  @Override
  public Item getDropItem() {
    return null;
  }

  public void destroy(DamageSource damageSource) {
    this.kill(null);
  }

  protected void moveOnRail(BlockPos pos, BlockState state) {
    // TODO: Fix moveOnRail - method removed in MC 26.2

    // super.moveOnRail(pos, state);
  }

  @Override
  public void tick() {
    super.tick();
  }

  @Override
  public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
    return false;
  }

  @Override
  public boolean canCollideWith(Entity entity) {
    return false;
  }

  @Override
  public boolean isPushable() {
    return false;
  }

  public boolean collidesWith(Entity other) {
    return false;
  }
}
