package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.NomadServerEntity;
// TODO: Fabric API - import net.fabricmc.fabric.api.event.Event;
// TODO: Fabric API - import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public interface NomadServerClicked {
  // TODO: Fabric API Event - Uncomment after updating Fabric API
  /*

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
  */

}
