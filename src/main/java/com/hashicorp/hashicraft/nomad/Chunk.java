package com.hashicorp.hashicraft.nomad;

import java.io.Serializable;
import java.util.ArrayList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

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
