package com.hashicorp.hashicraft.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class CartNbtData {

    public static final String DEFAULT_APPLICATION = "payments";
    public static final String DEFAULT_NOMAD_DEPLOYMENT = "payments-deployment";
    public static final String DEFAULT_NOMAD_NAMESPACE = "default";

    private final String name;
    private final String version;
    private final String nomadDeployment;

    public CartNbtData(String name, String version, String nomadDeployment) {
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

    public static CartNbtData getCustomNbt(ItemStack stack) {
        NbtCompound identity = stack.getOrCreateNbt();
        String name = identity.getString("name");
        String version = identity.getString("version");
        String nomadDeployment = identity.getString("nomad_deployment");
        return new CartNbtData(name, version, nomadDeployment);
    }

    public void setCustomNbt(ItemStack stack) {
        NbtCompound identity = stack.getOrCreateNbt();
        identity.putString("name", name);
        identity.putString("version", version);
        identity.putString("nomad_deployment", nomadDeployment);
        stack.setNbt(identity);
    }
}
