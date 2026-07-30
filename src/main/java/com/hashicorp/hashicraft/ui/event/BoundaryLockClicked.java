package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.BoundaryLockEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;

public interface BoundaryLockClicked {
    Event<BoundaryLockClicked> EVENT = EventFactory.createArrayBacked(BoundaryLockClicked.class,
            (listeners) -> (block, callback) -> {
                for (BoundaryLockClicked listener : listeners) {
                    InteractionResult result = listener.interact(block, callback);

                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });

    InteractionResult interact(BoundaryLockEntity block, SaveCallback callback);
}
