package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.VaultDispenserEntity;
// TODO: Fabric API - import net.fabricmc.fabric.api.event.Event;
// TODO: Fabric API - import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public interface VaultDispenserClicked {
  // TODO: Fabric API Event - Uncomment after updating Fabric API
  /*

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
  */

}
