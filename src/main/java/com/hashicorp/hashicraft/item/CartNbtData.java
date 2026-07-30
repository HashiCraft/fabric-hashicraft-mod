package com.hashicorp.hashicraft.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

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
        CompoundTag identity = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        String name = identity.getString("name").orElse("");
        String version = identity.getString("version").orElse("");
        String nomadDeployment = identity.getString("nomad_deployment").orElse("");
        return new CartNbtData(name, version, nomadDeployment);
    }

    public void setCustomNbt(ItemStack stack) {
        CompoundTag identity = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        identity.putString("name", name);
        identity.putString("version", version);
        identity.putString("nomad_deployment", nomadDeployment);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(identity));
    }
}
