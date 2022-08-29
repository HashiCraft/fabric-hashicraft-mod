package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.VaultLockEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface VaultLockClicked {
  Event<VaultLockClicked> EVENT = EventFactory.createArrayBacked(VaultLockClicked.class,
      (listeners) -> (block, callback) -> {
        for (VaultLockClicked listener : listeners) {
          ActionResult result = listener.interact(block, callback);

          if (result != ActionResult.PASS) {
            return result;
          }
        }

        return ActionResult.PASS;
      });

  ActionResult interact(VaultLockEntity block, SaveCallback callback);
}
