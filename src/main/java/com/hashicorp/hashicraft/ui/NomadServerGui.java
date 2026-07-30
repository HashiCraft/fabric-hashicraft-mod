package com.hashicorp.hashicraft.ui;

import com.hashicorp.hashicraft.block.entity.NomadServerEntity;
import com.hashicorp.hashicraft.ui.event.SaveCallback;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.network.chat.Component;

public class NomadServerGui extends LightweightGuiDescription {
    public NomadServerGui(NomadServerEntity server, SaveCallback callback) {
        String address = server.getAddress();

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel label = new WLabel(Component.literal("Address"));
        root.add(label, 0, 0, 4, 1);

        WTextField addressField;
        addressField = new WTextField(Component.literal("The Nomad server address"));
        root.add(addressField, 0, 1, 16, 2);
        addressField.setMaxLength(255);

        WLabel error = new WLabel(Component.literal(""));
        root.add(error, 0, 3, 16, 1);

        WButton button = new WButton(Component.literal("Save"));
        button.setOnClick(() -> {
            String Component = addressField.getText();
            if (!Component.isEmpty()) {
                server.setAddress(Component);
                callback.onSave();
            }
        });

        root.add(button, 0, 4, 16, 1);

        if (address != null) {
            addressField.setText(address);
        }

        root.validate(this);
    }
}
