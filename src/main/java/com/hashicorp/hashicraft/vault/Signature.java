package com.hashicorp.hashicraft.vault;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.hashicorp.hashicraft.Mod;

import java.io.Serializable;

public class Signature implements Serializable {
    public class Data {
        public String signature;
    }

    public Data data;

    public Signature() {
    }

    public byte[] toBytes() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        return json.getBytes();
    }

    public static Signature fromBytes(byte[] data) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Signature.class, new SignatureDataCreator());

            String json = new String(data);

            Gson gson = builder.create();
            Signature state = gson.fromJson(json, Signature.class);

            return state;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();

            String json = new String(data);
            Mod.LOGGER.error("Unable to create Signature from JSON:" + json);
            return null;
        }
    }
}
