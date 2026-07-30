package com.hashicorp.hashicraft.ui;

import com.hashicorp.hashicraft.block.entity.BoundaryLockEntity;
import com.hashicorp.hashicraft.ui.event.SaveCallback;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.Minecraft;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public class BoundaryLockGui extends LightweightGuiDescription {
    public BoundaryLockGui(BoundaryLockEntity lock, SaveCallback callback) {
        String address = lock.getAddress();

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel label = new WLabel(Component.literal("Address"));
        root.add(label, 0, 0, 4, 1);

        WTextField addressField;
        addressField = new WTextField(Component.literal("Address to connect to"));
        root.add(addressField, 0, 1, 16, 2);
        addressField.setMaxLength(255);

        WButton button = new WButton(Component.literal("Save"));
        button.setOnClick(() -> {
            String Component = addressField.getText();
            if (!Component.isEmpty()) {
                lock.setAddress(Component);
            } else {
                lock.setAddress("");
            }

            callback.onSave();

            // Close the screen
            Minecraft.getInstance().setScreenAndShow(null);
        });

        root.add(button, 0, 7, 16, 1);

        if (address != null) {
            addressField.setText(address);
        }

        root.validate(this);
    }
}
