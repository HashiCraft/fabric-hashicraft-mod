package com.hashicorp.hashicraft.nomad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.Serializable;

public class Evaluation implements Serializable {
    public String ID;
    public String Name;
    public String ClientStatus;
    public String NodeID;
    public String NodeName;
    public String JobID;
    public String JobType;
    public String TaskGroup;

    public Evaluation() {
    }

    public byte[] toBytes() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        return json.getBytes();
    }

    public static Evaluation fromBytes(byte[] data) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Evaluation.class, new AllocationDataCreator());

            String json = new String(data);

            Gson gson = builder.create();
            Evaluation state = gson.fromJson(json, Evaluation.class);

            return state;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            String json = new String(data);
            System.out.println("Unable to create Allocation from JSON:" + json);
            return null;
        }
    }
}