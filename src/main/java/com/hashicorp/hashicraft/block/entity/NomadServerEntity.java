package com.hashicorp.hashicraft.block.entity;

import java.util.Map.Entry;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.hashicorp.hashicraft.block.Blocks;
import com.hashicorp.hashicraft.block.NomadAlloc;
import com.hashicorp.hashicraft.block.NomadServer;
import com.hashicorp.hashicraft.block.NomadWhiskers;
import com.hashicorp.hashicraft.watcher.Allocation;
import com.hashicorp.hashicraft.watcher.Node;
import com.hashicorp.hashicraft.watcher.Watcher;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class NomadServerEntity extends StatefulBlockEntity {
  public NomadServerEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.NOMAD_SERVER_ENTITY, pos, state, null);
  }

  public NomadServerEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.NOMAD_SERVER_ENTITY, pos, state, parent);
  }

  public void destroy() {
    for (Node node : Watcher.getNodes().values()) {
      BlockPos nodePos = node.getPos();
      for (int x = nodePos.getX() + 1; x < nodePos.getX() + 14; x++) {
        for (int z = nodePos.getZ() + 1; z < nodePos.getZ() + 14; z++) {
          for (int y = nodePos.getY(); y < 255; y++) {
            BlockPos blockPos = new BlockPos(x, y, z);
            world.removeBlock(blockPos, false);
            world.removeBlockEntity(blockPos);
          }
        }
      }
    }
  }

  public static void tick(World world, BlockPos blockPos, BlockState state, NomadServerEntity entity) {
    StatefulBlockEntity.tick(world, blockPos, state, entity);
    if (!world.isClient) {
      entity.update(state);
    }
  }

  private void update(BlockState state) {
    for (Node node : Watcher.getNodes().values()) {
      for (Entry<String, String> entry : node.getSlots().entrySet()) {
        Allocation allocation = Watcher.getAllocations().get(entry.getValue());
        if (allocation != null) {
          BlockPos slotPos = node.getSlotPos(entry.getKey());

          if (allocation.JobID.contentEquals("finicky-whiskers")) {
            placeWhiskers(state, slotPos);
          } else {
            placeAllocation(state, slotPos, allocation.JobID);
          }
          // if (world.getBlockState(slotPos).isAir()) {
          // }
        }
      }
    }
  }

  private void placeWhiskers(BlockState state, BlockPos origin) {
    Direction facing = state.get(NomadServer.FACING);

    BlockPos pos = origin.add(2, 0, 2);
    BlockState w = Blocks.NOMAD_WHISKERS_BLOCK.getDefaultState().with(NomadWhiskers.FACING, facing);
    world.setBlockState(pos, w, Block.NOTIFY_ALL);
  }

  private void placeAllocation(BlockState state, BlockPos origin, String name) {
    Direction facing = state.get(NomadServer.FACING);

    // Wires
    // BlockState w1 = Blocks.NOMAD_WIRES_BLOCK.getDefaultState();
    // world.setBlockState(origin.add(0, 0, 0), w1, Block.NOTIFY_ALL);

    // BlockState w2 = Blocks.NOMAD_WIRES_BLOCK.getDefaultState();
    // world.setBlockState(origin.add(1, 0, 0), w2, Block.NOTIFY_ALL);

    // BlockState w3 = Blocks.NOMAD_WIRES_BLOCK.getDefaultState();
    // world.setBlockState(origin.add(0, 0, 2), w3, Block.NOTIFY_ALL);

    // BlockState w4 = Blocks.NOMAD_WIRES_BLOCK.getDefaultState();
    // world.setBlockState(origin.add(1, 0, 2), w4, Block.NOTIFY_ALL);

    // BlockState w5 = Blocks.NOMAD_WIRES_BLOCK.getDefaultState();
    // world.setBlockState(origin.add(2, 0, 1), w5, Block.NOTIFY_ALL);

    // Whiskers
    // BlockState w =
    // Blocks.NOMAD_WHISKERS_BLOCK.getDefaultState().with(NomadWhiskers.FACING,
    // facing);
    // world.setBlockState(origin.add(2, 0, 2), w, Block.NOTIFY_ALL);

    // Consul
    // BlockState c =
    // Blocks.CONSUL_PROXY_BLOCK.getDefaultState().with(ConsulProxy.FACING, facing);
    // world.setBlockState(origin.add(1, 0, 1), c, Block.NOTIFY_ALL);

    // Spin
    // BlockState s =
    // Blocks.NOMAD_SPIN_BLOCK.getDefaultState().with(NomadSpin.FACING, facing);
    // world.setBlockState(origin.add(0, 0, 1), s, Block.NOTIFY_ALL);

    // Alloc
    BlockPos pos = origin.add(2, 0, 2);
    BlockState a = Blocks.NOMAD_ALLOC_BLOCK.getDefaultState().with(NomadAlloc.FACING, facing);
    world.setBlockState(pos, a, Block.NOTIFY_ALL);

    BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof NomadAllocEntity) {
      NomadAllocEntity entity = (NomadAllocEntity) blockEntity;
      entity.setName(name);
    }
  }
}
