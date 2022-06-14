package com.hashicorp.hashicraft.watcher;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class EventDataCreator implements InstanceCreator<Event> {
  @Override
  public Event createInstance(Type type) {
    return new Event();
  }
}
