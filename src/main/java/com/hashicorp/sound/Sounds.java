package com.hashicorp.sound;

import com.hashicorp.hashicraft.Mod;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Sounds {
  public static SoundEvent PHONE_RINGING = registerSoundEvent("phone_ringing");
  public static SoundEvent PHONE_TOUCH = registerSoundEvent("phone_touch");

  public static SoundEvent COMPUTER_TYPING = registerSoundEvent("computer_typing");

  public static SoundEvent MEOW = registerSoundEvent("meow");
  public static SoundEvent WRONG_ANSWER = registerSoundEvent("wrong_answer");
  public static SoundEvent CORRECT_ANSWER = registerSoundEvent("correct_answer");
  public static SoundEvent GAME_OVER = registerSoundEvent("game_over");

  // Music
  public static SoundEvent HASHICONF_MUSIC = registerSoundEvent("hashiconf_music");

  private static SoundEvent registerSoundEvent(String name) {
    Identifier id = new Identifier(Mod.MOD_ID, name);
    return Registry.register(Registry.SOUND_EVENT, id, new SoundEvent(id));
  }

  public static void register() {
  }
}
