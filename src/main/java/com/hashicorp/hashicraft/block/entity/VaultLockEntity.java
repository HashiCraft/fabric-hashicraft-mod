package com.hashicorp.hashicraft.block.entity;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.vault.Decrypted;
import com.hashicorp.hashicraft.vault.Secret;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;

public class VaultLockEntity extends StatefulBlockEntity {
  public static final String vaultAddress = System.getenv().getOrDefault("VAULT_ADDR", "http://localhost:8200");
  public static final String vaultToken = System.getenv().getOrDefault("VAULT_TOKEN", "root");

  @Syncable
  // the path of the Vault secret
  private String path = "/secret/mykeys";

  @Syncable
  // the value name within the secret containing the key
  private String value = "key";

  @Syncable
  // the value of the key to compare the secret value to
  private String key = "mysecretkey";

  public void setPath(String path) {
    this.path = path;
    this.markForUpdate();
  }

  public String getPath() {
    return this.path;
  }

  public void setValue(String value) {
    this.value = value;
    this.markForUpdate();
  }

  public String getValue() {
    return this.value;
  }

  public void setKey(String key) {
    this.key = key;
    this.markForUpdate();
  }

  public String getKey() {
    return this.key;
  }

  public VaultLockEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.VAULT_LOCK_ENTITY, pos, state, null);
  }

  public VaultLockEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.VAULT_LOCK_ENTITY, pos, state, parent);
  }

  public boolean checkAccess(String token, String policy) {
    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(vaultAddress + "/v1/" + path))
          .header("Accept", "application/json")
          .header("X-Vault-Token", token)
          .GET()
          .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      if (response.statusCode() >= 400) {
        Mod.LOGGER.warn(response.body());
        return false;
      }

      Mod.LOGGER.info(response.body());

      // check the value of the key in the secret
      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      Secret secret = gson.fromJson(response.body(), Secret.class);

      if (secret.getData().getData().get(value).equals(key)) {
        return true;
      }

      return false;
    } catch (Exception e) {
      Mod.LOGGER.warn(e.getStackTrace().toString());
      return false;
    }
  }

  public boolean verify(String input, String signature) {
    try {
      String payload = String.format("""
          {
            "hash_algorithm":"sha2-256",
            "signature_algorithm":"pkcs1v15",
            "input":"%s",
            "signature": "%s"
          }
          """, Base64.getEncoder().encodeToString(input.getBytes()), signature);

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(vaultAddress + "/v1/transit/verify/minecraft"))
          .header("Accept", "application/json")
          .header("X-Vault-Token", vaultToken)
          .POST(HttpRequest.BodyPublishers.ofString(payload))
          .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      if (response.statusCode() >= 400) {
        Mod.LOGGER.warn(response.body());
        return false;
      }

      Mod.LOGGER.info(response.body());

      return true;
    } catch (Exception e) {
      Mod.LOGGER.warn(e.getStackTrace().toString());
      return false;
    }
  }

  public String decrypt(String input) {
    try {
      String payload = String.format("""
          {
            "ciphertext": "%s"
          }
          """, input);

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(vaultAddress + "/v1/transit/decrypt/minecraft"))
          .header("Accept", "application/json")
          .header("X-Vault-Token", vaultToken)
          .POST(HttpRequest.BodyPublishers.ofString(payload))
          .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      if (response.statusCode() >= 400) {
        Mod.LOGGER.warn(response.body());
        return null;
      }

      Mod.LOGGER.info(response.body());

      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      Decrypted decrypted = gson.fromJson(response.body(), Decrypted.class);
      return decrypted.data.plaintext;
    } catch (Exception e) {
      Mod.LOGGER.warn(e.getStackTrace().toString());
      return null;
    }
  }
}
