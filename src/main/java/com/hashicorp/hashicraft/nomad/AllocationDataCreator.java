package com.hashicorp.hashicraft.nomad;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class AllocationDataCreator implements InstanceCreator<Allocation> {
    @Override
    public Allocation createInstance(Type type) {
        return new Allocation();
    }
}
