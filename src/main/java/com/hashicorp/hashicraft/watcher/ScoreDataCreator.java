package com.hashicorp.hashicraft.watcher;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class ScoreDataCreator implements InstanceCreator<Score> {
  @Override
  public Score createInstance(Type type) {
    return new Score();
  }
}
