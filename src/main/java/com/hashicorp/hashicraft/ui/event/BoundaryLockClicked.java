package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.BoundaryLockEntity;
// TODO: Fabric API - import net.fabricmc.fabric.api.event.Event;
// TODO: Fabric API - import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public interface BoundaryLockClicked {
  // TODO: Fabric API Event - Uncomment after updating Fabric API
  /*

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
  */

}
