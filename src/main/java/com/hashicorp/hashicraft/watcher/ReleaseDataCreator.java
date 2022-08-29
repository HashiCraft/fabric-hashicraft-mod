package com.hashicorp.hashicraft.watcher;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class ReleaseDataCreator implements InstanceCreator<Release> {
  @Override
  public Release createInstance(Type type) {
    return new Release();
  }
}
