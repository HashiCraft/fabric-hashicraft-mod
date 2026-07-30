package com.hashicorp.hashicraft.ui;

import com.hashicorp.hashicraft.block.entity.VaultLockEntity;
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

public class VaultLockGui extends LightweightGuiDescription {
  public VaultLockGui(VaultLockEntity lock, SaveCallback callback) {

    WGridPanel root = new WGridPanel();
    setRootPanel(root);
    root.setInsets(Insets.ROOT_PANEL);

    WLabel labelPath = new WLabel(Component.literal("Path"));
    root.add(labelPath, 0, 0, 4, 1);

    WTextField pathField;
    pathField = new WTextField(Component.literal("Path to Vault secret required for access"));
    root.add(pathField, 0, 1, 16, 2);
    pathField.setMaxLength(255);

    WLabel labelValue = new WLabel(Component.literal("Value"));
    root.add(labelValue, 0, 3, 4, 1);

    WTextField valueField;
    valueField = new WTextField(Component.literal("Name of the Value in the secret containing the key"));
    root.add(valueField, 0, 4, 16, 2);
    valueField.setMaxLength(255);

    WLabel labelKey = new WLabel(Component.literal("Key"));
    root.add(labelKey, 0, 6, 4, 1);

    WTextField keyField;
    keyField = new WTextField(Component.literal("Key to compare the secret value to"));
    root.add(keyField, 0, 7, 16, 2);
    keyField.setMaxLength(255);

    WButton button = new WButton(Component.literal("Save"));
    button.setOnClick(() -> {
      String p = pathField.getText();
      if (!p.isEmpty()) {
        lock.setPath(p);
      }

      String v = valueField.getText();
      if (!v.isEmpty()) {
        lock.setValue(v);
      }

      String k = keyField.getText();
      if (!k.isEmpty()) {
        lock.setKey(k);
      }

      callback.onSave();

      Minecraft.getInstance().gui.setScreen(null);
      Minecraft.getInstance().gui.setScreen((Screen) null);
    });

    root.add(button, 0, 9, 16, 1);

    // set the defaults
    String path = lock.getPath();
    String value = lock.getValue();
    String key = lock.getKey();

    if (path != null) {
      pathField.setText(path);
    }

    if (value != null) {
      valueField.setText(value);
    }

    if (key != null) {
      keyField.setText(key);
    }

    root.validate(this);
  }
}
