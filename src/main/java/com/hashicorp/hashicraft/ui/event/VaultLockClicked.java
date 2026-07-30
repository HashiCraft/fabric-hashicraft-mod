package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.VaultLockEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;

public interface VaultLockClicked {
    Event<VaultLockClicked> EVENT = EventFactory.createArrayBacked(VaultLockClicked.class,
            (listeners) -> (block, callback) -> {
                for (VaultLockClicked listener : listeners) {
                    InteractionResult result = listener.interact(block, callback);

                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });

    InteractionResult interact(VaultLockEntity block, SaveCallback callback);
}
