package com.hashicorp.hashicraft.watcher;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class Allocation implements Serializable {
  public String ID;
  public String Name;
  public String ClientStatus;
  public String NodeID;
  public String NodeName;
  public String JobID;
  public String JobType;
  public String TaskGroup;

  public Allocation() {
  }

  public byte[] toBytes() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(this);
    return json.getBytes();
  }

  public static Allocation fromBytes(byte[] data) {
    try {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Allocation.class, new AllocationDataCreator());

      String json = new String(data);

      Gson gson = builder.create();
      Allocation state = gson.fromJson(json, Allocation.class);

      return state;
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
      String json = new String(data);
      System.out.println("Unable to create Allocation from JSON:" + json);
      return null;
    }
  }
}