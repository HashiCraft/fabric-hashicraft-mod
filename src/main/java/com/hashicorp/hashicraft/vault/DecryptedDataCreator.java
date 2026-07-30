package com.hashicorp.hashicraft.vault;

import com.google.gson.InstanceCreator;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import java.lang.reflect.Type;
public class DecryptedDataCreator implements InstanceCreator<Decrypted> {
    @Override
    public Decrypted createInstance(Type type) {
        return new Decrypted();
    }
}
