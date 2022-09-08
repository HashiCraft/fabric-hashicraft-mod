package com.hashicorp.hashicraft.consul;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class ReleaseDataCreator implements InstanceCreator<ReleaseStatus> {
  @Override
  public ReleaseStatus createInstance(Type type) {
    return new ReleaseStatus();
  }
}
