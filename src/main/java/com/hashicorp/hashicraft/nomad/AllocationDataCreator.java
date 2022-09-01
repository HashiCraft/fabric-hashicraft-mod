package com.hashicorp.hashicraft.nomad;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class AllocationDataCreator implements InstanceCreator<Allocation> {
  @Override
  public Allocation createInstance(Type type) {
    return new Allocation();
  }
}
