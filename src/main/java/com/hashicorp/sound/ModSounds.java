package com.hashicorp.sound;

import com.hashicorp.hashicraft.Mod;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
  // Boundary
  public static Identifier BOUNDARY_ALERT_ID = Mod.identifier("boundary_alert");
  public static SoundEvent BOUNDARY_ALERT = SoundEvent.of(BOUNDARY_ALERT_ID);

  public static void register() {
    Registry.register(Registries.SOUND_EVENT, BOUNDARY_ALERT_ID, BOUNDARY_ALERT);
  }
}
