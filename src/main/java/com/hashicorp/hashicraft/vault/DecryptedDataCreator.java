package com.hashicorp.hashicraft.vault;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class DecryptedDataCreator implements InstanceCreator<Decrypted> {
    @Override
    public Decrypted createInstance(Type type) {
        return new Decrypted();
    }
}
