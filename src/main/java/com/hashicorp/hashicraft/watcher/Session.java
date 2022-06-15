package com.hashicorp.hashicraft.watcher;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class Session implements Serializable {
  public String id;
  public ArrayList<MenuItem> menu;

  public Session() {
  }

  public byte[] toBytes() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(this);
    return json.getBytes();
  }

  public static Session fromBytes(byte[] data) {
    try {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Session.class, new SessionDataCreator());

      String json = new String(data);

      Gson gson = builder.create();
      Session state = gson.fromJson(json, Session.class);

      return state;
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
      String json = new String(data);
      System.out.println("Unable to create Session from JSON:" + json);
      return null;
    }
  }
}