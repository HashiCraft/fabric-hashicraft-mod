package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.NomadDispenserEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;

public interface NomadDispenserClicked {
    Event<NomadDispenserClicked> EVENT = EventFactory.createArrayBacked(NomadDispenserClicked.class,
            (listeners) -> (block, callback) -> {
                for (NomadDispenserClicked listener : listeners) {
                    InteractionResult result = listener.interact(block, callback);

                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });

    InteractionResult interact(NomadDispenserEntity block, SaveCallback callback);
}
