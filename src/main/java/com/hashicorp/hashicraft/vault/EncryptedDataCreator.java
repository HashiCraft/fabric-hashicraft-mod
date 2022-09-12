package com.hashicorp.hashicraft.vault;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class EncryptedDataCreator implements InstanceCreator<Encrypted> {
    @Override
    public Encrypted createInstance(Type type) {
        return new Encrypted();
    }
}
