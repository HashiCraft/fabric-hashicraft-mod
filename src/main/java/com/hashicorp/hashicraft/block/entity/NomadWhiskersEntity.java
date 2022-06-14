package com.hashicorp.hashicraft.block.entity;

import java.util.Random;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.hashicorp.hashicraft.block.NomadWhiskers;
import com.hashicorp.sound.Sounds;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NomadWhiskersEntity extends StatefulBlockEntity {
  @Syncable
  public Integer time = 0;

  public long ticks = 0;
  public Integer next = 5;

  private Random generator = new Random();
  private String[] food = { "beef", "chicken", "vegetable", "fish" };

  @Syncable
  private String currentFood = food[0];

  public NomadWhiskersEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.NOMAD_WHISKERS_ENTITY, pos, state, null);
  }

  public NomadWhiskersEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.NOMAD_WHISKERS_ENTITY, pos, state, parent);
  }

  public boolean isCountdown() {
    return time <= 4;
  }

  public boolean isEnded() {
    return time >= 30;
  }

  public String getMessage() {
    if (time <= 3) {
      return "" + time;
    } else if (time == 4) {
      return "GO!";
    } else {
      return "END";
    }
  }

  public int getCurrentTime() {
    return this.time;
  }

  public int getNextTime() {
    int high = 5;
    int low = 3;
    return generator.nextInt(high - low) + low;
  }

  public String getNextFood() {
    return food[generator.nextInt(food.length)];
  }

  public String getCurrentFood() {
    return currentFood;
  }

  public static void tick(World world, BlockPos blockPos, BlockState state, NomadWhiskersEntity entity) {
    if (state.get(NomadWhiskers.ACTIVE)) {
      entity.time = Math.round(entity.ticks++ / 20);

      if (entity.time >= 32) {
        world.setBlockState(blockPos, state.with(NomadWhiskers.ACTIVE, false));
        world.playSound(null, blockPos, Sounds.GAME_OVER, SoundCategory.BLOCKS, 0.3f, 1f);

        entity.time = 0;
        entity.ticks = 0;
        entity.next = 5;
        entity.currentFood = entity.food[0];

        return;
      } else if (entity.time >= entity.next) {
        entity.next = entity.time + entity.getNextTime();
        entity.currentFood = entity.getNextFood();
      }

      entity.setPropertiesToState();
      entity.sync();
    }
  }
}
