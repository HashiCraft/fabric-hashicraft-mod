package com.hashicorp.hashicraft.block.entity;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.block.BoundaryLockBlock;
import com.hashicorp.sound.ModSounds;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BoundaryLockEntity extends StatefulBlockEntity {
    @Syncable
    private String address = "http://boundary-proxy.container.shipyard.run:38500";

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
        Mod.LOGGER.info("Starting background thread - Boundary");
        if (executor == null || executor.isShutdown()) {
            executor = Executors.newFixedThreadPool(1);
            executor.submit(() -> {
                try {
                    while (!executor.isShutdown() && !executor.isTerminated()) {
                        if (!address.isEmpty()) {
                            boolean hasAccess = checkAccess();
                            if (hasAccess) {
                                world.playSound(
                                        null,
                                        pos,
                                        ModSounds.BOUNDARY_ALERT,
                                        SoundCategory.BLOCKS,
                                        1f,
                                        1f);
                            }

                            BlockPos pos = this.getPos();
                            BlockState newState = world.getBlockState(pos).with(BoundaryLockBlock.POWERED, hasAccess);
                            world.setBlockState(pos, newState, Block.NOTIFY_ALL);
                        } else {
                            Mod.LOGGER.info("Boundary proxy address not set, retrying in 5 seconds");
                        }

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
            Mod.LOGGER.info("Stopping background thread - Boundary");
            executor.shutdown();
        }
    }

    public boolean checkAccess() {
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("%s/v1/catalog/services", address)))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if (statusCode >= 400 || statusCode < 200) {
                Mod.LOGGER.warn("Cannot connect to Boundary target");
                return false;
            }

            Mod.LOGGER.info("Connected to Boundary target");
            return true;
        } catch (InterruptedException | IOException e) {
            Mod.LOGGER.warn("Cannot connect to Boundary proxy");
            return false;
        } catch (Exception e) {
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
