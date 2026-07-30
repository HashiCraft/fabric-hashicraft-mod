package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.VaultDispenserEntity;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;

public interface VaultDispenserClicked {
    Event<VaultDispenserClicked> EVENT = EventFactory.createArrayBacked(VaultDispenserClicked.class,
            (listeners) -> (block, callback) -> {
                for (VaultDispenserClicked listener : listeners) {
                    InteractionResult result = listener.interact(block, callback);

                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });

    InteractionResult interact(VaultDispenserEntity block, SaveCallback callback);
}
