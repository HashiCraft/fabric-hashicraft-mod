package com.hashicorp.hashicraft.nomad;

import java.lang.reflect.Type;

import com.google.gson.InstanceCreator;

public class EvaluationDataCreator implements InstanceCreator<Evaluation> {
  @Override
  public Evaluation createInstance(Type type) {
    return new Evaluation();
  }
}
