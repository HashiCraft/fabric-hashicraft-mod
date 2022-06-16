package com.hashicorp.hashicraft.block.entity;

import java.util.ArrayList;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.hashicorp.hashicraft.watcher.MenuItem;
import com.hashicorp.hashicraft.watcher.Session;
import com.hashicorp.hashicraft.watcher.Watcher;
import com.hashicorp.sound.Sounds;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NomadWhiskersEntity extends StatefulBlockEntity {
  @Syncable
  public Integer timer = 0;

  @Syncable
  public Integer score = 0;

  @Syncable
  public String session = "";

  @Syncable
  public String player = "";

  @Syncable
  public boolean inProgress = false;

  @Syncable
  public boolean inCountdown = false;

  @Syncable
  public boolean inEnding = false;

  public long ticks = 0;
  public int currentFoodIndex = 0;
  ArrayList<Integer> timing = new ArrayList<Integer>();
  ArrayList<String> menu = new ArrayList<String>();

  @Syncable
  private String currentFood = "";

  public NomadWhiskersEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.NOMAD_WHISKERS_ENTITY, pos, state, null);
  }

  public NomadWhiskersEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.NOMAD_WHISKERS_ENTITY, pos, state, parent);
  }

  public String getMessage() {
    if (inCountdown && timer != null) {
      if (timer <= 3) {
        return "" + timer;
      } else if (timer == 4) {
        return "GO!";
      }
    } else if (inEnding && timer != null) {
      return "END";
    }

    return "";
  }

  public String getCurrentFood() {
    return currentFood;
  }

  public int getCurrentTime() {
    if (this.timing.isEmpty()) {
      return 0;
    } else {
      return this.timing.get(this.currentFoodIndex);
    }
  }

  public static void tick(World world, BlockPos blockPos, BlockState state, NomadWhiskersEntity entity) {
    entity.timer = Math.round(entity.ticks++ / 20);
    if (entity.inCountdown) {
      if (entity.timer < 4) {
        entity.timer++;
        entity.setPropertiesToState();
        entity.sync();
      } else {
        entity.startSession();
      }
      return;
    }

    if (entity.inProgress) {
      if (entity.timer >= 30) {
        world.playSound(null, blockPos, Sounds.GAME_OVER, SoundCategory.BLOCKS, 0.3f, 1f);
        entity.endSession();
      }
      if (entity.timer * 1000 >= entity.getCurrentTime()) {
        if (entity.menu.isEmpty()) {
          return;
        }
        entity.currentFood = entity.menu.get(entity.currentFoodIndex);

        if (entity.currentFoodIndex < entity.menu.size() - 1) {
          entity.currentFoodIndex++;
        }
      }
      entity.setPropertiesToState();
      entity.sync();
      return;
    }

    if (entity.inEnding) {
      if (entity.timer < 3) {
        entity.timer++;
        entity.setPropertiesToState();
        entity.sync();
      } else {
        entity.submitScore();
        entity.setScore(0);
        entity.shutdown();
      }
      return;
    }
    StatefulBlockEntity.tick(world, blockPos, state, entity);
  }

  public void tally(String food, boolean correct) {
    Integer score = Watcher.tally(this.session, food, correct);
    this.setScore(score);
  }

  public void submitScore() {
    String id = this.getSession();
    String player = this.getPlayer();
    int score = this.getScore();

    Watcher.submitScore(id, player, score);
  }

  public void setScore(Integer score) {
    if (score == null) {
      this.score = 0;
    }
    this.score = score;
  }

  public int getScore() {
    if (this.score == null) {
      return 0;
    }
    return this.score;
  }

  public void startSession() {
    Session session = Watcher.startSession();
    if (session != null) {
      this.timer = 0;
      this.ticks = 0;
      this.currentFoodIndex = 0;
      this.setSession(session.id);
      this.setMenu(session.menu);
      this.setInCountdown(false);
      this.setInProgress(true);
      this.setPropertiesToState();
      this.sync();
    }
  }

  public void startCountdown() {
    this.timer = 0;
    this.ticks = 0;
    this.setInCountdown(true);
    this.setPropertiesToState();
    this.sync();
  }

  public void endSession() {
    this.timer = 0;
    this.ticks = 0;
    this.currentFoodIndex = 0;
    this.timing.clear();
    this.menu.clear();
    this.setInProgress(false);
    this.setInEnding(true);
    this.setPropertiesToState();
    this.sync();
  }

  public void shutdown() {
    this.setInEnding(false);
    this.setPropertiesToState();
    this.sync();
  }

  public void setInProgress(boolean status) {
    this.inProgress = status;
  }

  public void setInEnding(boolean status) {
    this.inEnding = status;
  }

  public void setInCountdown(boolean status) {
    this.inCountdown = status;
  }

  public void setMenu(ArrayList<MenuItem> menu) {
    int index = 0;
    for (MenuItem item : menu) {
      System.out.println(item.offset + " = " + item.demand);
      this.timing.add(index, item.offset);
      this.menu.add(index, item.demand);
      index++;
    }
  }

  public void setSession(String session) {
    this.session = session;
  }

  public String getSession() {
    if (this.session == null) {
      return "";
    }
    return this.session;
  }

  public void setPlayer(String player) {
    this.player = player;
  }

  public String getPlayer() {
    if (this.player == null) {
      return "";
    }
    return this.player;
  }
}
