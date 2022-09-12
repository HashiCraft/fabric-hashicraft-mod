package com.hashicorp.hashicraft.consul;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class ReleaseDataCreator implements InstanceCreator<ReleaseStatus> {
    @Override
    public ReleaseStatus createInstance(Type type) {
        return new ReleaseStatus();
    }
}
