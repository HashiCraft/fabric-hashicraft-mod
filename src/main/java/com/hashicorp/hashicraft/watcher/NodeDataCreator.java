package com.hashicorp.hashicraft.watcher;

import com.google.gson.InstanceCreator;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import java.lang.reflect.Type;
public class NodeDataCreator implements InstanceCreator<Node> {
    @Override
    public Node createInstance(Type type) {
        return new Node();
    }
}
