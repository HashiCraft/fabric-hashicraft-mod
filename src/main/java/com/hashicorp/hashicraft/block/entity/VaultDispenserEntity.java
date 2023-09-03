package com.hashicorp.hashicraft.block.entity;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.environment.Env;
import com.hashicorp.hashicraft.vault.Encrypted;
import com.hashicorp.hashicraft.vault.Login;
import com.hashicorp.hashicraft.vault.Signature;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointerImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;

public class VaultDispenserEntity extends StatefulBlockEntity {
  public static final String vaultAddress = System.getenv().getOrDefault("VAULT_ADDR", "http://localhost:8200");
  public static final String vaultToken = System.getenv().getOrDefault("VAULT_TOKEN", "root");
  public static final String vaultNamespace = System.getenv().getOrDefault("VAULT_NAMESPACE", "");

  @Syncable
  // userpass could contain a variable substitution like ${env.userpass}
  // this requires us to read that value from the environment before using
  private String userpass = "userpass";

  public void setUserpass(String userpass) {
    this.userpass = userpass;
    this.markForUpdate();
  }

  public String getUserpass() {
    return this.userpass;
  }

  // replace any environment variables in the userpass string
  private String resolveUserpass() {
    return Env.getValueSubstitution(this.userpass);
  }

  public VaultDispenserEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.VAULT_DISPENSER_ENTITY, pos, state, null);
  }

  public VaultDispenserEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.VAULT_DISPENSER_ENTITY, pos, state, parent);
  }

  public Login login(PlayerEntity player) {
    try {
      String username = player.getName().getString();
      String password = player.getUuidAsString();

      String payload = String.format("""
          {
            "password": "%s"
          }
          """, password);

      HttpClient client = HttpClient.newHttpClient();
      Builder builder = HttpRequest.newBuilder()
          .uri(URI.create(vaultAddress + "/v1/auth/" + resolveUserpass() + "/login/" + username))
          .header("Accept", "application/json")
          .header("X-Vault-Token", vaultToken);

      // if a namespace is set, use it
      if (vaultNamespace != "") {
        builder = builder.header("X-Vault-Namespace", vaultNamespace);
      }

      HttpRequest request = builder.POST(HttpRequest.BodyPublishers.ofString(payload))
          .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      if (response.statusCode() >= 400) {
        Mod.LOGGER.warn(response.body());
        return null;
      }

      Mod.LOGGER.debug(response.body());

      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      Login login = gson.fromJson(response.body(), Login.class);
      return login;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public String encrypt(String input) {
    try {
      String payload = String.format("""
          {
            "plaintext": "%s"
          }
          """, Base64.getEncoder().encodeToString(input.getBytes()));

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(vaultAddress + "/v1/transit/encrypt/minecraft"))
          .header("Accept", "application/json")
          .header("X-Vault-Token", vaultToken)
          .POST(HttpRequest.BodyPublishers.ofString(payload))
          .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      if (response.statusCode() >= 400) {
        Mod.LOGGER.warn(response.body());
        return null;
      }

      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      Encrypted encrypted = gson.fromJson(response.body(), Encrypted.class);
      return encrypted.data.ciphertext;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public String sign(String input) {
    try {
      String payload = String.format("""
          {
            "hash_algorithm":"sha2-256",
            "signature_algorithm":"pkcs1v15",
            "input":"%s"
          }
          """, Base64.getEncoder().encodeToString(input.getBytes()));

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(vaultAddress + "/v1/transit/sign/minecraft"))
          .header("Accept", "application/json")
          .header("X-Vault-Token", vaultToken)
          .POST(HttpRequest.BodyPublishers.ofString(payload))
          .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      if (response.statusCode() >= 400) {
        Mod.LOGGER.warn(response.body());
        return null;
      }

      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      Signature signature = gson.fromJson(response.body(), Signature.class);
      return signature.data.signature;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public void dispense(World world, BlockPointerImpl pointer, ItemStack stack, int offset, Direction side) {
    double x = pointer.getX() + 0.7D * (double) side.getOffsetX();
    double y = pointer.getY() + 0.7D * (double) side.getOffsetY();
    double z = pointer.getZ() + 0.7D * (double) side.getOffsetZ();

    if (side.getAxis() == Direction.Axis.Y) {
      y -= 0.425D;
    } else {
      y -= 0.45625D;
    }

    ItemEntity entity = new ItemEntity(world, x, y, z, stack);
    double g = world.random.nextDouble() * 0.1D + 0.2D;
    entity.setVelocity(
        world.random.nextGaussian() * 0.007499999832361937D * (double) offset + (double) side.getOffsetX() * g,
        world.random.nextGaussian() * 0.007499999832361937D * (double) offset + 0.20000000298023224D,
        world.random.nextGaussian() * 0.007499999832361937D * (double) offset + (double) side.getOffsetZ() * g);
    world.spawnEntity(entity);
  }

}
