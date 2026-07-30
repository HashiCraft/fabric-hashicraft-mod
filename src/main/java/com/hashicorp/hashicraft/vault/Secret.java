package com.hashicorp.hashicraft.vault;

import java.util.Map;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class Secret {
  private Data data;

  public Data getData() {
    return data;
  }

  public class Data {
    private Map<String, String> data;

    public Map<String, String> getData() {
      return data;
    }
  }
}
