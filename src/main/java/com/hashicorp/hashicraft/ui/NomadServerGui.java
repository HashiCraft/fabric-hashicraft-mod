package com.hashicorp.hashicraft.ui;

import com.hashicorp.hashicraft.block.entity.NomadServerEntity;
import com.hashicorp.hashicraft.ui.event.SaveCallback;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.text.Text;

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
                server.setAddress(text);
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
