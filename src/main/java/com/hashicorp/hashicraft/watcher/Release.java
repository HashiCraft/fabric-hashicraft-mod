package com.hashicorp.hashicraft.watcher;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import net.minecraft.util.math.BlockPos;

public class Release implements Serializable {
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

  private BlockPos pos;

  public Release() {
  }

  public byte[] toBytes() {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(this);
    return json.getBytes();
  }

  public static Release fromBytes(byte[] data) {
    try {
      GsonBuilder builder = new GsonBuilder();
      builder.registerTypeAdapter(Release.class, new ReleaseDataCreator());

      String json = new String(data);

      Gson gson = builder.create();
      Release state = gson.fromJson(json, Release.class);

      return state;
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
      String json = new String(data);
      System.out.println("Unable to create Release from JSON:" + json);
      return null;
    }
  }

  public void setPos(int x, int y, int z) {
    this.pos = new BlockPos(x, y, z);
  }

  public BlockPos getPos() {
    return this.pos;
  }
}