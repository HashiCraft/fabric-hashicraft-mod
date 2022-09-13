package com.hashicorp.hashicraft.watcher;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class NodeDataCreator implements InstanceCreator<Node> {
    @Override
    public Node createInstance(Type type) {
        return new Node();
    }
}
