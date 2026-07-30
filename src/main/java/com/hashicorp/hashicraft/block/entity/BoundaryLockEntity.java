package com.hashicorp.hashicraft.block.entity;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.block.BoundaryLockBlock;
import com.hashicorp.sound.ModSounds;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;

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
    public void setLevel(Level level) {
        super.setLevel(level);
        if (level != null && !level.isClientSide()) {
            this.start();
        }
    }

    // Used to stop the server thread that checks access.
    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.level != null && !this.level.isClientSide()) {
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
                                // TODO: Fix playSound - requires SoundEvent
                                // this.level.playSound(
                                //         null,
                                //         this.worldPosition,
                                //         ModSounds.BOUNDARY_ALERT,
                                //         SoundSource.BLOCKS,
                                //         1f,
                                //         1f);
                            }

                            BlockPos blockPos = this.worldPosition;
                            BlockState newState = this.level.getBlockState(blockPos).setValue(BoundaryLockBlock.POWERED, hasAccess);
                            this.level.setBlock(blockPos, newState, Block.UPDATE_ALL);
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
