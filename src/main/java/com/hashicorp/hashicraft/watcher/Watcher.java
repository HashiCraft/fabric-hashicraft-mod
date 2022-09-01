package com.hashicorp.hashicraft.watcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.hashicorp.hashicraft.nomad.Allocation;

// import fi.iki.elonen.NanoHTTPD;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
// import net.minecraft.world.World;

public class Watcher {
  public static final String nomadAddress = System.getenv().getOrDefault("NOMAD_ADDR", "http://localhost:4646");
  public static final BlockPos nomadOrigin = new BlockPos(10, 73, 32);
  public static final ArrayList<String> nomadJobs = new ArrayList<String>(
      Arrays.asList("payments", "finicky-whiskers", "api"));
  private static HashMap<String, Node> nodes = new HashMap<String, Node>();
  private static HashMap<String, Allocation> allocations = new HashMap<String, Allocation>();
  // private static HashMap<String, BlockPos> positions = new HashMap<String,
  // BlockPos>();
  // private static ArrayList<String> deleted = new ArrayList<String>();

  public static final String releaserAddress = System.getenv().getOrDefault("RELEASER_ADDR",
      "http://localhost:9443");
  public static final BlockPos releaserOrigin = new BlockPos(80, 64, 85);
  private static HashMap<String, Release> releases = new HashMap<String, Release>();

  private static Watcher watcher = new Watcher();

  public static Watcher getInstance() {
    return watcher;
  }

  // private static TrustManager[] trustAllCerts = new TrustManager[] {
  // new X509TrustManager() {
  // public java.security.cert.X509Certificate[] getAcceptedIssuers() {
  // return null;
  // }

  // public void checkClientTrusted(
  // java.security.cert.X509Certificate[] certs, String authType) {
  // }

  // public void checkServerTrusted(
  // java.security.cert.X509Certificate[] certs, String authType) {
  // }
  // }
  // };

  public static void Start(MinecraftServer server) {
    // Listen for Nomad event stream.
    // Listener listener = new Listener("0.0.0.0", 3333);
    // try {
    // listener.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    // } catch (IOException e) {
    // Mod.LOGGER.error("Port 3333 is already in use");
    // listener.stop();
    // }

    // updateNodes();

    // ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
    // executor.scheduleWithFixedDelay(() -> {
    // updateConsulReleaser(server);
    // }, 0, 5, TimeUnit.SECONDS);

    // executor.scheduleWithFixedDelay(() -> {
    // updateAllocations(server);
    // deleteAllocations(server);
    // }, 0, 5, TimeUnit.SECONDS);
  }

  //
  // Consul
  //
  // private static void updateConsulReleaser(MinecraftServer server) {
  // try {
  // SSLContext sslContext = SSLContext.getInstance("TLS");
  // sslContext.init(null, trustAllCerts, new SecureRandom());

  // HttpClient client = HttpClient
  // .newBuilder()
  // .sslContext(sslContext)
  // .build();

  // GsonBuilder builder = new GsonBuilder();
  // Gson gson = builder.create();

  // Type releaseListType = new TypeToken<ArrayList<Release>>() {
  // }.getType();

  // HttpRequest request = HttpRequest.newBuilder()
  // .uri(URI.create(releaserAddress + "/v1/releases"))
  // .headers("Accept", "application/json")
  // .GET()
  // .build();

  // HttpResponse<String> releaseResponse = client.send(request,
  // BodyHandlers.ofString());
  // ArrayList<Release> list = gson.fromJson(releaseResponse.body(),
  // releaseListType);

  // int index = 0;
  // for (Release release : list) {
  // switch (index) {
  // case 0:
  // BlockPos br = releaserOrigin.add(0, 0, 0);
  // release.setPos(br.getX(), br.getY(), br.getZ());
  // break;
  // case 1:
  // BlockPos bl = releaserOrigin.add(3, 0, 0);
  // release.setPos(bl.getX(), bl.getY(), bl.getZ());
  // break;
  // case 2:
  // BlockPos tr = releaserOrigin.add(0, 0, -3);
  // release.setPos(tr.getX(), tr.getY(), tr.getZ());
  // break;
  // case 3:
  // BlockPos tl = releaserOrigin.add(3, 0, -3);
  // release.setPos(tl.getX(), tl.getY(), tl.getZ());
  // break;
  // default:
  // BlockPos none = releaserOrigin.add(0, 255, 0);
  // release.setPos(none.getX(), none.getY(), none.getZ());
  // }

  // releases.put(release.Name, release);
  // index++;
  // }
  // } catch (Exception e) {
  // Mod.LOGGER.warn("Could not update releases");
  // }
  // }

  public static HashMap<String, Release> getReleases() {
    return releases;
  }

  public static Release getRelease(String name) {
    return releases.get(name);
  }

  //
  // Nomad
  //
  public static HashMap<String, Node> getNodes() {
    return nodes;
  }

  public static HashMap<String, Allocation> getAllocations() {
    return allocations;
  }

  // private static void deleteAllocations(MinecraftServer server) {
  // World world = server.getOverworld();

  // for (String id : deleted) {
  // removeArea(world, positions.get(id), 2, 2);

  // nodes.get(allocations.get(id).NodeID).removeAllocation(id);

  // positions.remove(id);
  // allocations.remove(id);
  // }

  // deleted.clear();
  // }

  // private static void removeArea(World world, BlockPos start, int width, int
  // length) {
  // for (int w = 0; w <= width; w++) {
  // for (int l = 0; l <= length; l++) {
  // world.removeBlock(start.add(w, 0, l), false);
  // world.removeBlockEntity(start.add(w, 0, l));
  // }
  // }
  // }

  // private static synchronized void updateAllocations(MinecraftServer server) {
  // HttpClient client = HttpClient.newHttpClient();

  // GsonBuilder builder = new GsonBuilder();
  // Gson gson = builder.create();

  // Type allocListType = new TypeToken<ArrayList<Allocation>>() {
  // }.getType();

  // HttpRequest allocRequest = HttpRequest.newBuilder()
  // .uri(URI.create(nomadAddress + "/v1/allocations"))
  // .headers("Accept", "application/json")
  // .GET()
  // .build();

  // try {
  // HttpResponse<String> allocResponse = client.send(allocRequest,
  // BodyHandlers.ofString());
  // ArrayList<Allocation> newList = gson.fromJson(allocResponse.body(),
  // allocListType);

  // List<Allocation> list = newList.stream()
  // .filter(a -> !a.ClientStatus.equalsIgnoreCase("complete"))
  // .collect(Collectors.toList());
  // List<String> ids = list.stream().map(a -> a.ID).collect(Collectors.toList());

  // // update all existing allocations
  // // add all new allocations
  // for (Allocation alloc : list) {
  // synchronized (Watcher.class) {

  // boolean interested = nomadJobs.stream().anyMatch(job ->
  // alloc.JobID.startsWith(job));
  // if (interested) {
  // allocations.put(alloc.ID, alloc);

  // Node node = nodes.get(alloc.NodeID);
  // String slot = node.placeAllocation(alloc.ID);
  // BlockPos position = node.getSlotPos(slot);
  // positions.put(alloc.ID, position);
  // }
  // }
  // }

  // // Mark all stopped allocations for removal.
  // for (String id : allocations.keySet()) {
  // synchronized (Watcher.class) {
  // if (!ids.contains(id)) {
  // deleted.add(id);
  // }
  // }
  // }
  // } catch (Exception e) {
  // Mod.LOGGER.warn("Could not update allocations");
  // Mod.LOGGER.debug(e.getStackTrace().toString());
  // }
  // }

  // private static synchronized void updateNodes() {
  // HttpClient client = HttpClient.newHttpClient();

  // GsonBuilder builder = new GsonBuilder();
  // Gson gson = builder.create();

  // Type nodeListType = new TypeToken<ArrayList<Node>>() {
  // }.getType();

  // HttpRequest request = HttpRequest.newBuilder()
  // .uri(URI.create(nomadAddress + "/v1/nodes"))
  // .headers("Accept", "application/json")
  // .GET()
  // .build();

  // try {
  // HttpResponse<String> nodeResponse = client.send(request,
  // BodyHandlers.ofString());
  // ArrayList<Node> list = gson.fromJson(nodeResponse.body(), nodeListType);

  // int index = 0;
  // for (Node node : list) {
  // int x = nomadOrigin.getX() + 2 + (index++ * 20);
  // int y = nomadOrigin.getY();
  // int z = nomadOrigin.getZ() + 2;
  // node.setPos(x, y, z);

  // nodes.put(node.ID, node);
  // }
  // } catch (Exception e) {
  // Mod.LOGGER.warn("Could not update nodes");
  // }
  // }
}