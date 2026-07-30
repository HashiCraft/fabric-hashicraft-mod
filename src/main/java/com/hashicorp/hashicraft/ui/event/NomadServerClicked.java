package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.NomadServerEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;

public interface NomadServerClicked {
    Event<NomadServerClicked> EVENT = EventFactory.createArrayBacked(NomadServerClicked.class,
            (listeners) -> (block, callback) -> {
                for (NomadServerClicked listener : listeners) {
                    InteractionResult result = listener.interact(block, callback);

                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });

    InteractionResult interact(NomadServerEntity block, SaveCallback callback);
}
