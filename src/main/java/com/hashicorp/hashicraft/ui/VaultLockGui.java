package com.hashicorp.hashicraft.ui;

import com.hashicorp.hashicraft.block.entity.VaultLockEntity;
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

public class VaultLockGui extends LightweightGuiDescription {
  public VaultLockGui(VaultLockEntity lock, SaveCallback callback) {

    WGridPanel root = new WGridPanel();
    setRootPanel(root);
    root.setInsets(Insets.ROOT_PANEL);

    WLabel labelPath = new WLabel(Text.literal("Path"));
    root.add(labelPath, 0, 0, 4, 1);

    WTextField pathField;
    pathField = new WTextField(Text.literal("Path to Vault secret required for access"));
    root.add(pathField, 0, 1, 16, 2);
    pathField.setMaxLength(255);

    WLabel labelValue = new WLabel(Text.literal("Value"));
    root.add(labelValue, 0, 3, 4, 1);

    WTextField valueField;
    valueField = new WTextField(Text.literal("Name of the Value in the secret containing the key"));
    root.add(valueField, 0, 4, 16, 2);
    valueField.setMaxLength(255);

    WLabel labelKey = new WLabel(Text.literal("Key"));
    root.add(labelKey, 0, 6, 4, 1);

    WTextField keyField;
    keyField = new WTextField(Text.literal("Key to compare the secret value to"));
    root.add(keyField, 0, 7, 16, 2);
    keyField.setMaxLength(255);

    WButton button = new WButton(Text.literal("Save"));
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

      MinecraftClient client = MinecraftClient.getInstance();
      client.player.closeScreen();
      MinecraftClient.getInstance().setScreen((Screen) null);
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
