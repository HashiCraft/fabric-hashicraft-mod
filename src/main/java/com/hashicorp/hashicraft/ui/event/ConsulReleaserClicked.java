package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.ConsulReleaserEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface ConsulReleaserClicked {
  Event<ConsulReleaserClicked> EVENT = EventFactory.createArrayBacked(ConsulReleaserClicked.class,
      (listeners) -> (block, callback) -> {
        for (ConsulReleaserClicked listener : listeners) {
          ActionResult result = listener.interact(block, callback);

          if (result != ActionResult.PASS) {
            return result;
          }
        }

        return ActionResult.PASS;
      });

  ActionResult interact(ConsulReleaserEntity block, SaveCallback callback);
}
