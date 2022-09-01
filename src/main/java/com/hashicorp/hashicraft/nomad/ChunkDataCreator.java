package com.hashicorp.hashicraft.nomad;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class ChunkDataCreator implements InstanceCreator<Chunk> {
  @Override
  public Chunk createInstance(Type type) {
    return new Chunk();
  }
}
