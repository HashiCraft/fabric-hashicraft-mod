package com.hashicorp.hashicraft.block.entity;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hashicorp.hashicraft.Mod;
import com.hashicorp.hashicraft.block.NomadServerBlock;
import com.hashicorp.hashicraft.entity.AppMinecartEntity;
import com.hashicorp.hashicraft.entity.ModEntities;
import com.hashicorp.hashicraft.nomad.Allocation;
import com.hashicorp.hashicraft.nomad.Chunk;
import com.hashicorp.hashicraft.nomad.Job;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class NomadServerEntity extends StatefulBlockEntity {
  @Syncable
  private String address = "http://server.local.nomad-cluster.shipyard.run:4646";

  private final Set<String> allocations = new HashSet<>();

  private ExecutorService executor;

  public NomadServerEntity(BlockPos pos, BlockState state) {
    super(BlockEntities.NOMAD_SERVER_ENTITY, pos, state, null);
  }

  public NomadServerEntity(BlockPos pos, BlockState state, Block parent) {
    super(BlockEntities.NOMAD_SERVER_ENTITY, pos, state, parent);
  }

  // Used to start the server thread that streams events.
  @Override
  public void setWorld(World world) {
    super.setWorld(world);
    if (!world.isClient) {
      this.start();
    }
  }

  // Used to stop the server thread that streams events.
  @Override
  public void markRemoved() {
    super.markRemoved();
    if (this.hasWorld() && !this.getWorld().isClient) {
      this.stop();
    }
  }

  public synchronized void start() {
    Mod.LOGGER.info("Starting background thread - Nomad");
    if (executor == null || executor.isShutdown()) {
      executor = Executors.newFixedThreadPool(1);
      executor.submit(() -> {
        try {
          while (!executor.isShutdown() && !executor.isTerminated()) {
            if (!address.isEmpty()) {
              TimeUnit.SECONDS.sleep(5);

              int index = getAllocations();
              getEvents(index);
            } else {
              Mod.LOGGER.info("Nomad address not set, retrying in 5 seconds" + executor.toString());
            }

            TimeUnit.SECONDS.sleep(5);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
    }
  }

  public void stop() {
    if (executor != null && !executor.isShutdown()) {
      // Get rid of all carts still hanging around.
      destroyAllCarts();

      Mod.LOGGER.info("Stopping background thread - Nomad");
      executor.shutdown();
    }
  }

  public void sendStatusMessage(String message) {
    Box box = new Box(this.getPos()).expand(32.0);
    List<PlayerEntity> players = this.getWorld().getEntitiesByClass(PlayerEntity.class, box,
        (player) -> {
          return true;
        });

    for (PlayerEntity player : players) {
      player.sendMessage(Text.literal(message), true);
    }
  }

  public int getAllocations() {
    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(address + "/v1/allocations?filter=ClientStatus%3D%3Drunning"))
          .header("Accept", "application/json")
          .GET()
          .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      if (response.statusCode() >= 404) {
        Mod.LOGGER.warn(response.body());
        return -1;
      }

      GsonBuilder builder = new GsonBuilder();
      Gson gson = builder.create();

      Type allocationListType = new TypeToken<ArrayList<Allocation>>() {
      }.getType();

      ArrayList<Allocation> list = gson.fromJson(response.body(), allocationListType);
      for (Allocation allocation : list) {
        if (createCart(allocation.ID, allocation.JobID, getVersionMetadata(allocation.JobID))) {
          allocations.add(allocation.ID);
          wait(2);
        }
      }

      String index = response.headers().firstValue("X-Nomad-Index").orElse("-1");
      Mod.LOGGER.info("Starting off at index " + index);

      return Integer.parseInt(index);
    } catch (ConnectException e) {
      Mod.LOGGER.warn("Cannot connect to Nomad");
      return -1;
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }

  private String getVersionMetadata(String jobID) {
    Job.Spec job = getJob(jobID);
    return job.Meta != null ? job.Meta.getOrDefault("version", "") : "";
  }

  private void wait(int duration) {
    try {
      TimeUnit.SECONDS.sleep(duration);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<Allocation> getEvents(int index) {
    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(String.format("%s/v1/event/stream?topic=Allocation&index=%d", address, index)))
          .header("Accept", "application/json")
          .GET()
          .build();

      HttpResponse<Stream<String>> response = client.send(request,
          BodyHandlers.ofLines());
      if (response.statusCode() >= 400) {
        Mod.LOGGER.warn("status >= 400");
        return null;
      }

      response.body().forEach((body) -> {
        if (executor.isShutdown())
          // Stop parsing events when the thread has shut down.
          return;

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Chunk chunk = gson.fromJson(body, Chunk.class);

        // Check if we have an empty response.
        if (chunk.Index == 0)
          return;

        chunk.Events.forEach((event) -> {
          switch (event.Topic) {
            case "Allocation":
              Allocation allocation = event.Payload.Allocation;
              // Check if we need to create a new cart.
              if (allocation.ClientStatus.equalsIgnoreCase("pending")
                  && allocation.DesiredStatus.equalsIgnoreCase("run")) {
                if (!allocations.contains(allocation.ID)) {
                  Mod.LOGGER.info("[pending -> run] Creating allocation: " + allocation.ID);
                  if (createCart(allocation.ID, allocation.JobID,
                      getVersionMetadata(allocation.JobID)))
                    allocations.add(allocation.ID);
                  wait(2);
                } else {
                  Mod.LOGGER.info("[pending -> run] Restarting allocation: " + allocation.ID);
                  if (destroyCart(allocation.ID)) {
                    allocations.remove(allocation.ID);
                    wait(2);

                    if (createCart(allocation.ID, allocation.JobID,
                        getVersionMetadata(allocation.JobID)))
                      allocations.add(allocation.ID);
                    wait(2);
                  }
                }
              }
              // Check if we need to destroy an existing cart.
              else if (allocation.ClientStatus.equalsIgnoreCase("running")
                  && allocation.DesiredStatus.equalsIgnoreCase("stop")) {
                if (allocations.contains(allocation.ID)) {
                  Mod.LOGGER.info("[running -> stop] Destroying allocation: " + allocation.ID);
                  if (destroyCart(allocation.ID))
                    allocations.remove(allocation.ID);
                } else {
                  Mod.LOGGER
                      .info("[running -> stop] Destroying unknown allocation: " + allocation.ID);
                  if (destroyCart(allocation.ID))
                    allocations.remove(allocation.ID);
                }
              } else if (allocation.ClientStatus.equalsIgnoreCase("failed")
                  && allocation.DesiredStatus.equalsIgnoreCase("stop")) {
                if (allocations.contains(allocation.ID)) {
                  Mod.LOGGER.info("[failed -> stop] Destroying allocation: " + allocation.ID);
                  destroyCart(allocation.ID);
                  allocations.remove(allocation.ID);
                } else {
                  Mod.LOGGER.info("[failed -> stop] Destroying unknown allocation: " + allocation.ID);
                  destroyCart(allocation.ID);
                  allocations.remove(allocation.ID);
                }
              } else {
                Mod.LOGGER.info("event id: " + event.Index +
                    " job id: " + allocation.JobID +
                    " status: " + allocation.ClientStatus +
                    " desired: " + allocation.DesiredStatus);
              }
              break;
            default:
              Mod.LOGGER.info("Uncaught event: " + event.Topic);
          }
        });
      });

      return null;
    } catch (ConnectException e) {
      Mod.LOGGER.warn("Cannot connect to Nomad");
      return null;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean stopAllocation(String id) {
    try {
      String payload = String.format("""
          {
            "signal": "SIGKILL"
          }
          """);

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(address + "/v1/client/allocation/" + id + "/signal"))
          .header("Accept", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(payload))
          .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      if (response.statusCode() >= 404) {
        Mod.LOGGER.warn(response.body());
        return false;
      }

      Mod.LOGGER.debug(response.body());

      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean createCart(String id, String application, String version) {
    Mod.LOGGER.info(String.format("Created cart: %s, %s, %s", id, application, version));
    BlockPos pos = this.getPos();
    BlockPos output = pos.west();
    BlockState blockState = world.getBlockState(output);
    if (!blockState.isIn(BlockTags.RAILS)) {
      Mod.LOGGER.warn("NOT RAILS!!!");
      return false;
    }
    RailShape railShape = blockState.getBlock() instanceof AbstractRailBlock
        ? blockState.get(((AbstractRailBlock) blockState.getBlock()).getShapeProperty())
        : RailShape.NORTH_SOUTH;
    double d = 0.0;
    if (railShape.isAscending()) {
      d = 0.5;
    }

    AppMinecartEntity entity = new AppMinecartEntity(ModEntities.APP_MINECART, world);
    entity.setPos(
        (double) output.getX() + 0.5,
        (double) output.getY() + 0.0625 + d,
        (double) output.getZ() + 0.5);

    entity.setAllocationID(Text.literal(id));
    entity.setApplication(Text.literal(application));
    entity.setVersion(Text.literal(version));
    world.spawnEntity(entity);
    this.getWorld().emitGameEvent(null, GameEvent.ENTITY_PLACE, output);

    BlockState newState = world.getBlockState(pos).with(NomadServerBlock.POWERED, true);
    world.setBlockState(pos, newState, Block.NOTIFY_ALL);
    world.scheduleBlockTick(pos, newState.getBlock(), 40);

    return true;
  }

  public void destroyAllCarts() {
    Mod.LOGGER.info("Destroying all carts");
    Box box = new Box(this.getPos()).expand(32.0);
    List<AppMinecartEntity> minecarts = this.getWorld().getEntitiesByClass(AppMinecartEntity.class, box,
        (entity) -> {
          return true;
        });

    minecarts.forEach((cart) -> {
      cart.discard();
    });
  }

  public boolean destroyCart(String id) {
    Box box = new Box(this.getPos()).expand(32.0);
    List<AppMinecartEntity> minecarts = this.getWorld().getEntitiesByClass(AppMinecartEntity.class, box,
        (entity) -> {
          return true;
        });

    minecarts.forEach((cart) -> {
      if (cart.getAllocationID().getString().equalsIgnoreCase(id)) {
        Mod.LOGGER.info(
            String.format("Destroyed cart: %s, %s, %s",
                cart.getAllocationID().getString(),
                cart.getApplication().getString(),
                cart.getVersion().getString()));
        cart.discard();
        this.getWorld().emitGameEvent(null, GameEvent.ENTITY_DIE, cart.getBlockPos());
      }
    });
    return true;
  }

  public boolean createJob(String application, String version, String nomadDeployment) {
    try {
      Job job = new Job(application, version, nomadDeployment);
      String payload = job.toJson();

      Mod.LOGGER.info("Submitting job to Nomad: " + nomadDeployment + ", " + version);

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(address + "/v1/jobs"))
          .header("Accept", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(payload))
          .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      if (response.statusCode() >= 400) {
        Mod.LOGGER.error(response.body());
        return false;
      }

      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public Job.Spec getJob(String jobID) {
    try {
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(String.format("%s/v1/job/%s", address, jobID)))
          .header("Accept", "application/json")
          .GET()
          .build();

      HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
      if (response.statusCode() >= 400) {
        Mod.LOGGER.error(response.body());
        return null;
      }

      Gson gson = new Gson();
      return gson.fromJson(response.body(), Job.Spec.class);

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public void setAddress(String address) {
    this.address = address;
    this.markForUpdate();
  }

  public String getAddress() {
    return this.address;
  }
}
