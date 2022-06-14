package com.hashicorp.hashicraft.watcher;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class NodeDataCreator implements InstanceCreator<Node> {
  @Override
  public Node createInstance(Type type) {
    return new Node();
  }
}
