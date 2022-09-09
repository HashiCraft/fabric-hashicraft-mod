package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.BoundaryLockEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface BoundaryLockClicked {
  Event<BoundaryLockClicked> EVENT = EventFactory.createArrayBacked(BoundaryLockClicked.class,
      (listeners) -> (block, callback) -> {
        for (BoundaryLockClicked listener : listeners) {
          ActionResult result = listener.interact(block, callback);

          if (result != ActionResult.PASS) {
            return result;
          }
        }

        return ActionResult.PASS;
      });

  ActionResult interact(BoundaryLockEntity block, SaveCallback callback);
}
