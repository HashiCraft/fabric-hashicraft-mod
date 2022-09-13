package com.hashicorp.hashicraft.ui;

import com.hashicorp.hashicraft.block.entity.NomadServerEntity;
import com.hashicorp.hashicraft.ui.event.SaveCallback;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NomadServerGui extends LightweightGuiDescription {
    public NomadServerGui(NomadServerEntity server, SaveCallback callback) {
        String address = server.getAddress();

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel label = new WLabel(Text.literal("Address"));
        root.add(label, 0, 0, 4, 1);

        WTextField addressField;
        addressField = new WTextField(Text.literal("The Nomad server address"));
        root.add(addressField, 0, 1, 16, 2);
        addressField.setMaxLength(255);

        WLabel error = new WLabel(Text.literal(""));
        root.add(error, 0, 3, 16, 1);

        WButton button = new WButton(Text.literal("Save"));
        button.setOnClick(() -> {
            String text = addressField.getText();
            if (!text.isEmpty()) {

                HttpURLConnection connection = null;
                try {
                    URL u = new URL(text);
                    connection = (HttpURLConnection) u.openConnection();
                    connection.setRequestMethod("HEAD");
                    connection.setConnectTimeout(1);
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        // If we could connect, set the address.
                        server.setAddress(text);
                        callback.onSave();

                        // Close the screen.
                        MinecraftClient client = MinecraftClient.getInstance();
                        client.player.closeScreen();
                        MinecraftClient.getInstance().setScreen((Screen) null);
                    } else {
                        error.setText(
                                Text.literal("Expected status 200, but got " + code).setStyle(Style.EMPTY.withColor(Formatting.RED)));
                    }
                } catch (MalformedURLException e) {
                    error.setText(Text.literal("URL is malformed").setStyle(Style.EMPTY.withColor(Formatting.RED)));
                } catch (IOException e) {
                    error.setText(Text.literal("Could not connect to " + text).setStyle(Style.EMPTY.withColor(Formatting.RED)));
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        });

        root.add(button, 0, 4, 16, 1);

        if (address != null) {
            addressField.setText(address);
        }

        root.validate(this);
    }
}
