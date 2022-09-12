package com.hashicorp.sound;

import com.hashicorp.hashicraft.Mod;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSounds {
    // Music
    public static SoundEvent HASHICONF_MUSIC = registerSoundEvent("hashiconf_music");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(Mod.MOD_ID, name);
        return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
    }

    public static void register() {
    }
}
