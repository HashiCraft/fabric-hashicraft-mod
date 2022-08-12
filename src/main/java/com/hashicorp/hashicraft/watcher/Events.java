package com.hashicorp.hashicraft.watcher;

import net.minecraft.util.Identifier;

public class Events {
  public static Identifier NOMAD_ADD_SERVER = new Identifier("hashicraft/nomad/addserver");
  public static Identifier NOMAD_REMOVE_SERVER = new Identifier("hashicraft/nomad/removeserver");
  public static Identifier NOMAD_NODE_DRAIN = new Identifier("hashicraft/nomad/nodedrain");
  public static Identifier NOMAD_REGISTER_JOB = new Identifier("hashicraft/nomad/registerjob");
}