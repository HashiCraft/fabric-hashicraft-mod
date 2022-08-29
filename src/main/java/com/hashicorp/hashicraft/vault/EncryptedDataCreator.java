package com.hashicorp.hashicraft.vault;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class EncryptedDataCreator implements InstanceCreator<Encrypted> {
  @Override
  public Encrypted createInstance(Type type) {
    return new Encrypted();
  }
}
