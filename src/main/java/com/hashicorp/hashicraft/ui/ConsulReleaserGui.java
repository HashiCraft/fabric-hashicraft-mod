package com.hashicorp.hashicraft.ui;

import com.hashicorp.hashicraft.block.entity.ConsulReleaserEntity;
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

public class ConsulReleaserGui extends LightweightGuiDescription {
    public ConsulReleaserGui(ConsulReleaserEntity releaser, SaveCallback callback) {
        String address = releaser.getAddress();
        String application = releaser.getApplication();
        String prometheusAddress = releaser.getPrometheusAddress();
        String nomadDeployment = releaser.getNomadDeployment();
        String nomadNamespace = releaser.getNomadNamespace();

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel addressLabel = new WLabel(Text.literal("Address"));
        root.add(addressLabel, 0, 0, 4, 1);

        WTextField addressField = new WTextField(Text.literal("Address of Consul releaser"));
        root.add(addressField, 0, 1, 8, 2);
        addressField.setMaxLength(255);
        addressField.setText(address);

        WLabel applicationLabel = new WLabel(Text.literal("Name"));
        root.add(applicationLabel, 0, 2, 4, 1);

        WTextField applicationField = new WTextField(Text.literal("Name of release"));
        root.add(applicationField, 0, 3, 8, 2);
        applicationField.setMaxLength(255);
        applicationField.setText(application);

        WLabel prometheusAddressLabel = new WLabel(Text.literal("Prometheus Address"));
        root.add(prometheusAddressLabel, 9, 2, 4, 1);

        WTextField prometheusAddressField = new WTextField(Text.literal("Address for Prometheus"));
        root.add(prometheusAddressField, 9, 3, 8, 2);
        prometheusAddressField.setMaxLength(255);
        prometheusAddressField.setText(prometheusAddress);

        WLabel nomadDeploymentLabel = new WLabel(Text.literal("Nomad Deployment Name"));
        root.add(nomadDeploymentLabel, 0, 4, 4, 1);

        WTextField nomadDeploymentField = new WTextField(Text.literal("Name of Nomad Deployment"));
        root.add(nomadDeploymentField, 0, 5, 8, 2);
        nomadDeploymentField.setMaxLength(255);
        nomadDeploymentField.setText(nomadDeployment);

        WLabel nomadNamespaceLabel = new WLabel(Text.literal("Nomad Deployment Namespace"));
        root.add(nomadNamespaceLabel, 9, 4, 4, 1);

        WTextField nomadNamespaceField = new WTextField(Text.literal("Namespace of Nomad Deployment"));
        root.add(nomadNamespaceField, 9, 5, 8, 2);
        nomadNamespaceField.setMaxLength(255);
        nomadNamespaceField.setText(nomadNamespace);

        WButton button = new WButton(Text.literal("Save"));
        button.setOnClick(() -> {
            String addressText = addressField.getText();
            if (!addressText.isEmpty()) {
                releaser.setAddress(addressText);
            }

            String applicationText = applicationField.getText();
            if (!applicationText.isEmpty()) {
                releaser.setApplication(applicationText);
            }

            String prometheusAddressText = prometheusAddressField.getText();
            if (!prometheusAddressText.isEmpty()) {
                releaser.setPrometheusAddress(prometheusAddressText);
            }

            String nomadDeploymentText = nomadDeploymentField.getText();
            if (!nomadDeploymentText.isEmpty()) {
                releaser.setNomadDeployment(nomadDeploymentText);
            }

            String nomadNamespaceText = nomadNamespaceField.getText();
            if (!nomadNamespaceText.isEmpty()) {
                releaser.setNomadNamespace(nomadNamespaceText);
            }

            // notify the opener that the dialog has completed
            callback.onSave();

            MinecraftClient client = MinecraftClient.getInstance();
            client.player.closeScreen();
            MinecraftClient.getInstance().setScreen((Screen) null);
        });

        root.add(button, 0, 7, 16, 1);

        if (address != null) {
            addressField.setText(address);
        }

        if (application != null) {
            applicationField.setText(application);
        }

        if (prometheusAddress != null) {
            prometheusAddressField.setText(prometheusAddress);
        }

        if (nomadDeployment != null) {
            nomadDeploymentField.setText(nomadDeployment);
        }

        if (nomadNamespace != null) {
            nomadNamespaceField.setText(nomadNamespace);
        }

        root.validate(this);
    }
}
