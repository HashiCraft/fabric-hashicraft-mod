package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.VaultDispenserEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface VaultDispenserClicked {
    Event<VaultDispenserClicked> EVENT = EventFactory.createArrayBacked(VaultDispenserClicked.class,
            (listeners) -> (block, callback) -> {
                for (VaultDispenserClicked listener : listeners) {
                    ActionResult result = listener.interact(block, callback);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(VaultDispenserEntity block, SaveCallback callback);
}
