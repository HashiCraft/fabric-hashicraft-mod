package com.hashicorp.hashicraft.ui;

import com.hashicorp.hashicraft.block.entity.VaultLockEntity;
import com.hashicorp.hashicraft.events.VaultLockGuiCallback;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class VaultLockGui extends LightweightGuiDescription {
  public VaultLockGui(VaultLockEntity lock, VaultLockGuiCallback callback) {
    String policy = lock.getPolicy();

    WGridPanel root = new WGridPanel();
    setRootPanel(root);
    root.setInsets(Insets.ROOT_PANEL);

    WLabel label = new WLabel(Text.literal("Policies"));
    root.add(label, 0, 0, 4, 1);

    WTextField policyField;
    policyField = new WTextField(Text.literal("Policies required for access"));
    root.add(policyField, 0, 1, 16, 2);
    policyField.setMaxLength(255);

    WButton button = new WButton(Text.literal("Save"));
    button.setOnClick(() -> {
      String text = policyField.getText();
      if (!text.isEmpty()) {
        lock.setPolicy(text);
      }

      // notify the opener that the dialog has completed
      callback.onSave();

      MinecraftClient.getInstance().player.closeScreen();
      MinecraftClient.getInstance().setScreen((Screen) null);
    });

    root.add(button, 0, 7, 16, 1);

    if (policy != null) {
      policyField.setText(policy);
    }

    root.validate(this);
  }
}
