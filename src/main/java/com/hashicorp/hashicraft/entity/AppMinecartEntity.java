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

  public AppMinecartEntity(EntityType<?> type, World world) {
    super(type, world);
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
}
