package com.hashicorp.hashicraft.watcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

public class Node implements Serializable {
    public String ID;
    public String Name;
    public String Status;

    private BlockPos pos;
    private HashMap<String, String> slots = new HashMap<String, String>();

    public Node() {
        slots.put("bottom_right", null);
        slots.put("bottom_left", null);
        slots.put("top_right", null);
        slots.put("top_left", null);
    }

    public byte[] toBytes() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        return json.getBytes();
    }

    public static Node fromBytes(byte[] data) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Node.class, new NodeDataCreator());

            String json = new String(data);

            Gson gson = builder.create();
            Node state = gson.fromJson(json, Node.class);

            return state;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            String json = new String(data);
            System.out.println("Unable to create Node from JSON:" + json);
            return null;
        }
    }

    public void setPos(int x, int y, int z) {
        this.pos = new BlockPos(x, y, z);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public BlockPos getSlotPos(String id) {
        switch (id) {
            case "bottom_right":
                return new BlockPos(this.pos.getX() + 3, this.pos.getY(), this.pos.getZ() + 3);
            case "bottom_left":
                return new BlockPos(this.pos.getX() + 10, this.pos.getY(), this.pos.getZ() + 3);
            case "top_right":
                return new BlockPos(this.pos.getX() + 3, this.pos.getY(), this.pos.getZ() + 10);
            case "top_left":
                return new BlockPos(this.pos.getX() + 10, this.pos.getY(), this.pos.getZ() + 10);
        }

        return this.pos;
    }

    public HashMap<String, String> getSlots() {
        return this.slots;
    }

    public void removeAllocation(String allocation) {
        HashMap<String, String> slots = this.getSlots();
        for (Entry<String, String> entry : slots.entrySet()) {
            if (entry.getValue() == allocation) {
                slots.replace(entry.getKey(), null);
            }
        }
    }

    public String placeAllocation(String allocation) {
        HashMap<String, String> slots = this.getSlots();
        if (slots.containsValue(allocation)) {
            for (Entry<String, String> entry : slots.entrySet()) {
                if (entry.getValue().equalsIgnoreCase(allocation)) {
                    return entry.getKey();
                }
            }
        } else {
            for (Entry<String, String> entry : slots.entrySet()) {
                if (entry.getValue() == null) {
                    slots.put(entry.getKey(), allocation);
                    return entry.getKey();
                }
            }
        }
        return "";
    }

    public String getAvailableSlot() {

        return null;
    }
}