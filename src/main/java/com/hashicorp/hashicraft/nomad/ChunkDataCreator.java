package com.hashicorp.hashicraft.nomad;

import com.google.gson.InstanceCreator;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import java.lang.reflect.Type;
public class ChunkDataCreator implements InstanceCreator<Chunk> {
    @Override
    public Chunk createInstance(Type type) {
        return new Chunk();
    }
}
