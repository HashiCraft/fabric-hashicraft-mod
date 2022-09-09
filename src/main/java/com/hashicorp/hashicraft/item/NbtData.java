package com.hashicorp.hashicraft.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class NbtData {

    public static final String DEFAULT_APPLICATION = "payments";
    public static final String DEFAULT_NOMAD_DEPLOYMENT = "payments-deployment";
    public static final String DEFAULT_NOMAD_NAMESPACE = "default";

    private String name;
    private String version;
    private String nomadDeployment;

    public NbtData(String name, String version, String nomadDeployment) {
        this.name = name;
        this.version = version;
        this.nomadDeployment = nomadDeployment;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getNomadDeployment() {
        return nomadDeployment;
    }

    public static NbtData getCustomNbt(ItemStack stack) {
        NbtCompound identity = stack.getOrCreateNbt();
        String name = identity.getString("name");
        String version = identity.getString("version");
        String nomadDeployment = identity.getString("nomad_deployment");
        return new NbtData(name, version, nomadDeployment);
    }

    public void setCustomNbt(ItemStack stack) {
        NbtCompound identity = stack.getOrCreateNbt();
        identity.putString("name", name);
        identity.putString("version", version);
        identity.putString("nomad_deployment", nomadDeployment);
        stack.setNbt(identity);
    }
}
