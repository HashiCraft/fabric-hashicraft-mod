package com.hashicorp.hashicraft.vault;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class SignatureDataCreator implements InstanceCreator<Signature> {
    @Override
    public Signature createInstance(Type type) {
        return new Signature();
    }
}
