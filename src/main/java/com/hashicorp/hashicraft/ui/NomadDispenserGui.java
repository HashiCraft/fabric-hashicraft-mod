package com.hashicorp.hashicraft.ui;

import com.hashicorp.hashicraft.block.entity.NomadDispenserEntity;
import com.hashicorp.hashicraft.ui.event.SaveCallback;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class NomadDispenserGui extends LightweightGuiDescription {
    public NomadDispenserGui(NomadDispenserEntity dispenser, SaveCallback callback) {
        String name = dispenser.getName();
        String color = dispenser.getColors();
        String nomadDeployment = dispenser.getNomadDeployment();

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel nameLabel = new WLabel(Text.literal("Application Name"));
        root.add(nameLabel, 0, 0, 4, 1);

        WTextField nameField = new WTextField();
        root.add(nameField, 0, 1, 8, 1);
        nameField.setMaxLength(255);

        WLabel nomadDeploymentLabel = new WLabel(Text.literal("Nomad Deployment"));
        root.add(nomadDeploymentLabel, 9, 0, 4, 1);

        WTextField nomadDeploymentField = new WTextField();
        root.add(nomadDeploymentField, 9, 1, 8, 1);
        nomadDeploymentField.setMaxLength(255);

        WLabel colorLabel = new WLabel(Text.literal("Colors (Application Versions). Comma-Separated."));
        root.add(colorLabel, 0, 3, 4, 1);

        WTextField colorField = new WTextField();
        root.add(colorField, 0, 4, 16, 1);
        colorField.setMaxLength(255);

        WButton button = new WButton(Text.literal("Save"));
        button.setOnClick(() -> {
            String nameText = nameField.getText();
            if (!nameText.isEmpty()) {
                dispenser.setName(nameText);
            }

            String nomadDeploymentText = nomadDeploymentField.getText();
            if (!nomadDeploymentText.isEmpty()) {
                dispenser.setNomadDeployment(nomadDeploymentText);
            }

            String colorText = colorField.getText();
            if (!colorText.isEmpty()) {
                dispenser.setColors(colorText);
            }

            callback.onSave();

            MinecraftClient client = MinecraftClient.getInstance();
            client.player.closeScreen();
            MinecraftClient.getInstance().setScreen((Screen) null);
        });

        root.add(button, 0, 7, 16, 1);

        if (name != null) {
            nameField.setText(name);
        }

        if (color != null) {
            colorField.setText(color);
        }

        if (nomadDeployment != null) {
            nomadDeploymentField.setText(nomadDeployment);
        }

        root.validate(this);
    }
}
