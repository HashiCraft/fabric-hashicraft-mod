package com.hashicorp.hashicraft.consul;

import com.hashicorp.hashicraft.Mod;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Releaser {
    HttpClient client;
    String uri;

    public Releaser(String address) {
        this.uri = address + "/v1/releases";
        this.client = HttpClient.newHttpClient();
    }

    public ReleaseStatus create(Release release) throws IOException, InterruptedException {
        String requestBody = release.toJson();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.uri))
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            Mod.LOGGER.warn(String.format("Could not create release. Body %s, got %d",
                    requestBody, response.statusCode()));
            return null;
        }
        return ReleaseStatus.fromBytes(response.body().getBytes());
    }

    public ReleaseStatus get(String name) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.uri + "/" + name))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            Mod.LOGGER.warn(String.format("Could not find release %s, got %d", name, response.statusCode()));
            return null;
        }
        return ReleaseStatus.fromBytes(response.body().getBytes());
    }

    public List<ReleaseStatus> list() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.uri))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            Mod.LOGGER.warn(response.body());
            return null;
        }
        return ReleaseStatus.toList(response.body());
    }

    public void delete(String name) throws IOException, InterruptedException {
        if (this.get(name) != null) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(this.uri + "/" + name))
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                Mod.LOGGER.warn(String.format("Could not delete release %s, got %d", name, response.statusCode()));
            }
            while (Objects.equals(this.get(name).Status, "state_destroy")) {
                Mod.LOGGER.warn(String.format("Waiting to delete release %s", name));
                TimeUnit.SECONDS.sleep(5);
            }
        }
    }
}
