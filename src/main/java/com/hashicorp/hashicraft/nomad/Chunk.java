package com.hashicorp.hashicraft.nomad;

import java.io.Serializable;
import java.util.ArrayList;

public class Chunk implements Serializable {
  public int Index;
  public ArrayList<Event> Events;

  public class Event {
    public String Topic;
    public String Type;
    public int Index;
    public Payload Payload;
  }

  public class Payload {
    public Allocation Allocation;
  }
}
