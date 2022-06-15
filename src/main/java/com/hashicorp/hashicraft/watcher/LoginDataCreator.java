package com.hashicorp.hashicraft.watcher;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class LoginDataCreator implements InstanceCreator<Login> {
  @Override
  public Login createInstance(Type type) {
    return new Login();
  }
}
