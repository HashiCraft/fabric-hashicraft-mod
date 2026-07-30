package com.hashicorp.hashicraft.ui;

import com.hashicorp.hashicraft.block.entity.VaultDispenserEntity;
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

public class VaultDispenserGui extends LightweightGuiDescription {
  public VaultDispenserGui(VaultDispenserEntity dispenser, SaveCallback callback) {
    String userpass = dispenser.getUserpass();

    WGridPanel root = new WGridPanel();
    setRootPanel(root);
    root.setInsets(Insets.ROOT_PANEL);

    WLabel label = new WLabel(Component.literal("Userpass Mount Path"));
    root.add(label, 0, 0, 4, 1);

    WTextField userpassField;
    userpassField = new WTextField(Component.literal("Mount point for userpass authentication method"));
    root.add(userpassField, 0, 1, 16, 2);
    userpassField.setMaxLength(255);

    WButton button = new WButton(Component.literal("Save"));
    button.setOnClick(() -> {
      String Component = userpassField.getText();
      if (!Component.isEmpty()) {
        dispenser.setUserpass(Component);
      }

      callback.onSave();

      // Close the screen
      Minecraft.getInstance().setScreenAndShow(null);
    });

    root.add(button, 0, 7, 16, 1);

    if (userpass != null) {
      userpassField.setText(userpass);
    }

    root.validate(this);
  }
}
