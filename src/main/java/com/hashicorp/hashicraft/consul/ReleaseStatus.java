package com.hashicorp.hashicraft.consul;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import com.google.gson.reflect.TypeToken;

public class ReleaseStatus implements Serializable {
  @SerializedName("name")
  public String Name;

  @SerializedName("version")
  public String Version;

  @SerializedName("candidate_traffic")
  public int CandidateTraffic;

  @SerializedName("status")
  public String Status;

  @SerializedName("last_deployment_status")
  public String DeploymentStatus;

  public ReleaseStatus() {}

  public byte[] toBytes() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(this);
    return json.getBytes();
  }

  public static ReleaseStatus fromBytes(byte[] data) {
    try {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(ReleaseStatus.class, new ReleaseDataCreator());

      String json = new String(data);

      Gson gson = builder.create();
      return gson.fromJson(json, ReleaseStatus.class);

    } catch (JsonSyntaxException e) {
      e.printStackTrace();
      String json = new String(data);
      System.out.println("Unable to create Release from JSON:" + json);
      return null;
    }
  }

  public static List<ReleaseStatus> toList(String json) {
      Gson gson = new Gson();
      return gson.fromJson(json, new TypeToken<ArrayList<ReleaseStatus>>(){}.getType());
  }
}