package com.hashicorp.hashicraft.watcher;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class SessionDataCreator implements InstanceCreator<Session> {
  @Override
  public Session createInstance(Type type) {
    return new Session();
  }
}
