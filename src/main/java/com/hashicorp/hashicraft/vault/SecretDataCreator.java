package com.hashicorp.hashicraft.vault;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class SecretDataCreator implements InstanceCreator<Secret> {
    @Override
    public Secret createInstance(Type type) {
        return new Secret();
    }
}
