package com.hashicorp.hashicraft.watcher;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class Score implements Serializable {
  public String ulid;
  public Integer total;

  public Score() {
  }

  public byte[] toBytes() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(this);
    return json.getBytes();
  }

  public static Score fromBytes(byte[] data) {
    try {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Score.class, new ScoreDataCreator());

      String json = new String(data);

      Gson gson = builder.create();
      Score state = gson.fromJson(json, Score.class);

      return state;
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
      String json = new String(data);
      System.out.println("Unable to create Score from JSON:" + json);
      return null;
    }
  }
}