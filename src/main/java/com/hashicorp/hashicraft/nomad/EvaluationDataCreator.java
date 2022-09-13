package com.hashicorp.hashicraft.nomad;

import com.google.gson.InstanceCreator;

import java.lang.reflect.Type;

public class EvaluationDataCreator implements InstanceCreator<Evaluation> {
    @Override
    public Evaluation createInstance(Type type) {
        return new Evaluation();
    }
}
