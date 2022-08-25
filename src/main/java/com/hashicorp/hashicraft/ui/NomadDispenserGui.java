package com.hashicorp.hashicraft.ui;

import com.hashicorp.hashicraft.block.entity.NomadDispenserEntity;
import com.hashicorp.hashicraft.events.NomadDispenserCallback;
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
    public NomadDispenserGui(NomadDispenserEntity dispenser, NomadDispenserCallback callback) {
        String name = dispenser.getName();
        String version = dispenser.getVersion();
        String color = dispenser.getColor();

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel nameLabel = new WLabel(Text.literal("Application Name"));
        root.add(nameLabel, 0, 0, 4, 1);

        WTextField nameField = new WTextField(Text.literal("Name added to application"));
        root.add(nameField, 0, 1, 16, 1);
        nameField.setMaxLength(255);

        WLabel versionLabel = new WLabel(Text.literal("Application Version"));
        root.add(versionLabel, 0, 2, 4, 1);

        WTextField versionField = new WTextField(Text.literal("Version added to application"));
        root.add(versionField, 0, 3, 16, 1);
        versionField.setMaxLength(255);

        WLabel colorLabel = new WLabel(Text.literal("Color"));
        root.add(colorLabel, 0, 4, 4, 1);

        WTextField colorField = new WTextField(Text.literal("Color added to dye"));
        root.add(colorField, 0, 5, 16, 1);
        colorField.setMaxLength(255);

        WButton button = new WButton(Text.literal("Save"));
        button.setOnClick(() -> {
            String nameText = nameField.getText();
            if (!nameText.isEmpty()) {
                dispenser.setName(nameText);
            }

            String versionText = versionField.getText();
            if (!versionText.isEmpty()) {
                dispenser.setVersion(versionText);
            }

            String colorText = colorField.getText();
            if (!colorText.isEmpty()) {
                dispenser.setColor(colorText);
            }

            callback.onSave();

            MinecraftClient.getInstance().player.closeScreen();
            MinecraftClient.getInstance().setScreen((Screen) null);
        });

        root.add(button, 0, 7, 16, 1);

        if (name != null) {
            nameField.setText(name);
        }

        if (version != null) {
            versionField.setText(version);
        }

        if (color != null) {
            colorField.setText(color);
        }

        root.validate(this);
    }
}
