package com.hashicorp.hashicraft.block.entity;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.block.BoundaryLockBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BoundaryLockEntity extends StatefulBlockEntity {
  @Syncable
  private String address = "";

  private ExecutorService executor;

  public BoundaryLockEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.BOUNDARY_LOCK_ENTITY, pos, state, null);
  }

  public BoundaryLockEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.BOUNDARY_LOCK_ENTITY, pos, state, parent);
  }

  // Used to start the server thread that checks access.
  @Override
  public void setWorld(World world) {
    super.setWorld(world);
    if (!world.isClient) {
      this.start();
    }
  }

  // Used to stop the server thread that checks access.
  @Override
  public void markRemoved() {
    super.markRemoved();
    if (this.hasWorld() && !this.getWorld().isClient) {
      this.stop();
    }
  }

  public synchronized void start() {
    Mod.LOGGER.info("Starting background thread");
    if (executor == null || executor.isShutdown()) {
      executor = Executors.newFixedThreadPool(1);
      executor.submit(() -> {
        try {
          while (!executor.isShutdown() && !executor.isTerminated()) {
            boolean hasAccess = checkAccess();

            BlockPos pos = this.getPos();
            BlockState newState = world.getBlockState(pos).with(BoundaryLockBlock.POWERED, hasAccess);
            world.setBlockState(pos, newState, Block.NOTIFY_ALL);

            TimeUnit.SECONDS.sleep(5);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
    }
  }

  public void stop() {
    if (executor != null && !executor.isShutdown()) {
      Mod.LOGGER.info("Stopping background thread");
      executor.shutdown();
    }
  }

  public boolean checkAccess() {
    if (address.isBlank()) {
      return false;
    }

    String[] parts = address.split(":");
    if (parts.length != 2) {
      return false;
    }

    try {
      String host = parts[0];
      int port = Integer.parseInt(parts[1]);

      SocketAddress socketAddress = new InetSocketAddress(host, port);
      Socket socket = new Socket();
      socket.connect(socketAddress, 1000);
      socket.close();

      return true;
    } catch (ConnectException e) {
      return false;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }

  }

  public void setAddress(String address) {
    this.address = address;
    this.markForUpdate();
  }

  public String getAddress() {
    return this.address;
  }
}
