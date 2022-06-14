package com.hashicorp.hashicraft.watcher;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hashicorp.hashicraft.Mod;

// import fi.iki.elonen.NanoHTTPD;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Watcher {
  public static final String nomadAddress = System.getenv("NOMAD_ADDR");
  public static final BlockPos nomadOrigin = new BlockPos(10, 73, 32);
  private static HashMap<String, Node> nodes = new HashMap<String, Node>();
  private static HashMap<String, Allocation> allocations = new HashMap<String, Allocation>();
  private static HashMap<String, BlockPos> positions = new HashMap<String, BlockPos>();
  private static ArrayList<String> deleted = new ArrayList<String>();

  public static final String releaserAddress = System.getenv("RELEASER_ADDR");
  public static final BlockPos releaserOrigin = new BlockPos(10, 73, 32);
  private static HashMap<String, Release> releases = new HashMap<String, Release>();

  public static final String vaultAddress = System.getenv("VAULT_ADDR");

  private static Watcher watcher = new Watcher();

  public static Watcher getInstance() {
    return watcher;
  }

  private static TrustManager[] trustAllCerts = new TrustManager[] {
      new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        public void checkClientTrusted(
            java.security.cert.X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(
            java.security.cert.X509Certificate[] certs, String authType) {
        }
      }
  };

  public static void Start(MinecraftServer server) {
    // Listen for Nomad event stream.
    // Listener listener = new Listener("0.0.0.0", 3333);
    // try {
    // listener.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    // } catch (IOException e) {
    // Mod.LOGGER.error("Port 3333 is already in use");
    // listener.stop();
    // }

    updateNodes();

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleWithFixedDelay(() -> {
      updateConsulReleaser(server);
    }, 0, 5, TimeUnit.SECONDS);

    executor.scheduleWithFixedDelay(() -> {
      updateNomad(server);
    }, 0, 5, TimeUnit.SECONDS);
  }

  //
  // Consul
  //
  private static void updateConsulReleaser(MinecraftServer server) {
    try {
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, trustAllCerts, new SecureRandom());

      HttpClient client = HttpClient
          .newBuilder()
          .sslContext(sslContext)
          .build();

      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();

      Type releaseListType = new TypeToken<ArrayList<Release>>() {
      }.getType();

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(releaserAddress + "/v1/releases"))
          .headers("Accept", "application/json")
          .GET()
          .build();

      HttpResponse<String> releaseResponse = client.send(request, BodyHandlers.ofString());
      ArrayList<Release> list = gson.fromJson(releaseResponse.body(), releaseListType);

      for (Release release : list) {
        System.out.println(release.Name);
        releases.put(release.Name, release);
      }
    } catch (Exception e) {
      Mod.LOGGER.warn("Could not update releases", e.getMessage());
      e.printStackTrace();
    }
  }

  public static HashMap<String, Release> getReleases() {
    return releases;
  }

  public static Release getRelease(String name) {
    return releases.get(name);
  }

  //
  // Vault
  //
  public static String login(String username, String password) {
    try {
      String payload = String.format("""
          {
          "password": "%s"
          }
          """, password);

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(vaultAddress + "/v1/auth/userpass/login/" + username))
          .header("Accept", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(payload))
          .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

      // Check if everything went well.
      if (response.statusCode() >= 400) {
        System.out.println(response.body());
        return null;
      }

      System.out.println(response.body());

      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();
      Map result = gson.fromJson(response.body(), Map.class);
      Map auth = (Map) result.get("auth");
      String token = (String) auth.get("client_token");

      return token;
    } catch (IOException | InterruptedException e) {
      System.out.println("ERROR: " + e.getMessage());
      return null;
    }
  }

  public static boolean checkAccess(String token, String policy) {
    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(vaultAddress + "/v1/secret/data/minecraft/" + policy))
          .header("Accept", "application/json")
          .header("X-Vault-Token", token)
          .GET()
          .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      // Check if everything went well.
      if (response.statusCode() >= 400) {
        System.out.println(response.body());
        return false;
      }

      System.out.println(response.body());
      return true;
    } catch (IOException | InterruptedException e) {
      System.out.println("ERROR: " + e.getMessage());
      return false;
    }
  }

  public static boolean createUserPass(String username, String password, String policies) {
    try {
      String payload = String.format("""
          {
          "password": "%s",
          "policies": "%s"
          }
          """, password, policies);

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(vaultAddress + "/v1/auth/userpass/users/" + username))
          .header("Accept", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(payload))
          .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      // Check if everything went well.
      if (response.statusCode() >= 400) {
        System.out.println(response.body());
        return false;
      }

      System.out.println(response.body());
      return true;
    } catch (IOException | InterruptedException e) {
      System.out.println("ERROR: " + e.getMessage());
      return false;
    }
  }

  //
  // Nomad
  //
  private static void updateNomad(MinecraftServer server) {
    updateAllocations(server);
    deleteAllocations(server);
  }

  public static HashMap<String, Node> getNodes() {
    return nodes;
  }

  public static HashMap<String, Allocation> getAllocations() {
    return allocations;
  }

  private static void deleteAllocations(MinecraftServer server) {
    World world = server.getOverworld();

    for (String id : deleted) {
      removeArea(world, positions.get(id), 2, 2);

      nodes.get(allocations.get(id).NodeID).removeAllocation(id);

      positions.remove(id);
      allocations.remove(id);
    }

    deleted.clear();
  }

  private static void removeArea(World world, BlockPos start, int width, int length) {
    for (int w = 0; w <= width; w++) {
      for (int l = 0; l <= length; l++) {
        world.removeBlock(start.add(w, 0, l), false);
        world.removeBlockEntity(start.add(w, 0, l));
      }
    }
  }

  private static synchronized void updateAllocations(MinecraftServer server) {
    HttpClient client = HttpClient.newHttpClient();

    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();

    Type allocListType = new TypeToken<ArrayList<Allocation>>() {
    }.getType();

    HttpRequest allocRequest = HttpRequest.newBuilder()
        .uri(URI.create(nomadAddress + "/v1/allocations"))
        .headers("Accept", "application/json")
        .GET()
        .build();

    try {
      HttpResponse<String> allocResponse = client.send(allocRequest, BodyHandlers.ofString());
      ArrayList<Allocation> newList = gson.fromJson(allocResponse.body(), allocListType);

      List<Allocation> list = newList.stream()
          .filter(a -> !a.ClientStatus.equalsIgnoreCase("complete"))
          .collect(Collectors.toList());
      List<String> ids = list.stream().map(a -> a.ID).collect(Collectors.toList());

      // update all existing allocations
      // add all new allocations
      for (Allocation alloc : list) {
        synchronized (Watcher.class) {
          allocations.put(alloc.ID, alloc);

          Node node = nodes.get(alloc.NodeID);
          String slot = node.placeAllocation(alloc.ID);
          BlockPos position = node.getSlotPos(slot);
          positions.put(alloc.ID, position);
        }
      }

      // Mark all stopped allocations for removal.
      for (String id : allocations.keySet()) {
        synchronized (Watcher.class) {
          if (!ids.contains(id)) {
            deleted.add(id);
          }
        }
      }
    } catch (InterruptedException e) {
      Mod.LOGGER.warn("Could not update allocations", e.getMessage());
    } catch (IOException e) {
      Mod.LOGGER.warn("Could not update allocations", e.getMessage());
    }
  }

  private static synchronized void updateNodes() {
    HttpClient client = HttpClient.newHttpClient();

    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();

    Type nodeListType = new TypeToken<ArrayList<Node>>() {
    }.getType();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(nomadAddress + "/v1/nodes"))
        .headers("Accept", "application/json")
        .GET()
        .build();

    try {
      HttpResponse<String> nodeResponse = client.send(request, BodyHandlers.ofString());
      ArrayList<Node> list = gson.fromJson(nodeResponse.body(), nodeListType);

      int index = 0;
      for (Node node : list) {
        int x = nomadOrigin.getX() + 2 + (index++ * 20);
        int y = nomadOrigin.getY();
        int z = nomadOrigin.getZ() + 2;
        node.setPos(x, y, z);

        nodes.put(node.ID, node);
      }
    } catch (InterruptedException e) {
      Mod.LOGGER.warn("Could not update nodes", e.getMessage());
    } catch (IOException e) {
      Mod.LOGGER.warn("Could not update nodes", e.getMessage());
    }
  }
}
