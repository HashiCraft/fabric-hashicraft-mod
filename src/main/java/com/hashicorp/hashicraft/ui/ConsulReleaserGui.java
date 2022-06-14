package com.hashicorp.hashicraft.ui;

import com.hashicorp.hashicraft.block.entity.ConsulReleaserEntity;
import com.hashicorp.hashicraft.events.ConsulReleaserGuiCallback;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.LiteralText;

public class ConsulReleaserGui extends LightweightGuiDescription {
  public ConsulReleaserGui(ConsulReleaserEntity releaser, ConsulReleaserGuiCallback callback) {
    String application = releaser.getApplication();

    WGridPanel root = new WGridPanel();
    setRootPanel(root);
    root.setInsets(Insets.ROOT_PANEL);

    WLabel label = new WLabel(new LiteralText("Application"));
    root.add(label, 0, 0, 4, 1);

    WTextField applicationField;
    applicationField = new WTextField(new LiteralText("Application to manage"));
    root.add(applicationField, 0, 1, 16, 2);
    applicationField.setMaxLength(255);
    applicationField.setText(application);

    WButton button = new WButton(new LiteralText("Save"));
    button.setOnClick(() -> {
      String text = applicationField.getText();
      if (!text.isEmpty()) {
        releaser.setApplication(text);
      }

      // notify the opener that the dialog has completed
      callback.onSave();

      MinecraftClient.getInstance().player.closeScreen();
      MinecraftClient.getInstance().setScreen((Screen) null);
    });

    root.add(button, 0, 7, 16, 1);

    // if (application != null) {

    // }

    root.validate(this);
  }
}
