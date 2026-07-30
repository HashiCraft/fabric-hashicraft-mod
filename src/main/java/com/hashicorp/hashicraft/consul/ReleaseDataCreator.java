package com.hashicorp.hashicraft.consul;

import com.google.gson.InstanceCreator;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import java.lang.reflect.Type;
public class ReleaseDataCreator implements InstanceCreator<ReleaseStatus> {
    @Override
    public ReleaseStatus createInstance(Type type) {
        return new ReleaseStatus();
    }
}
