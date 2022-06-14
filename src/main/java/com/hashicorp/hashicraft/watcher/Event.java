package com.hashicorp.hashicraft.watcher;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class Event implements Serializable {
  public String event;
  public String topic;
  public String source;
  public String timestamp;
  public Object payload;

  public Event() {
  }

  public byte[] toBytes() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(this);
    return json.getBytes();
  }

  public static Event fromBytes(byte[] data) {
    try {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Event.class, new EventDataCreator());

      String json = new String(data);

      Gson gson = builder.create();
      Event state = gson.fromJson(json, Event.class);

      return state;
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
      String json = new String(data);
      System.out.println("Unable to create Event from JSON:" + json);
      return null;
    }
  }
}