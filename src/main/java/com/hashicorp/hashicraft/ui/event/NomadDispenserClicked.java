package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.NomadDispenserEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;

public interface NomadDispenserClicked {
    Event<NomadDispenserClicked> EVENT = EventFactory.createArrayBacked(NomadDispenserClicked.class,
            (listeners) -> (block, callback) -> {
                for (NomadDispenserClicked listener : listeners) {
                    ActionResult result = listener.interact(block, callback);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(NomadDispenserEntity block, SaveCallback callback);
}
