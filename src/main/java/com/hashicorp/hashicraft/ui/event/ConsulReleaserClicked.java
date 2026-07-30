package com.hashicorp.hashicraft.ui.event;

import com.hashicorp.hashicraft.block.entity.ConsulReleaserEntity;
// TODO: Fabric API - import net.fabricmc.fabric.api.event.Event;
// TODO: Fabric API - import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

public interface ConsulReleaserClicked {
  // TODO: Fabric API Event - Uncomment after updating Fabric API
  /*

    Event<ConsulReleaserClicked> EVENT = EventFactory.createArrayBacked(ConsulReleaserClicked.class,
            (listeners) -> (block, callback) -> {
                for (ConsulReleaserClicked listener : listeners) {
                    InteractionResult result = listener.interact(block, callback);

                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });

    InteractionResult interact(ConsulReleaserEntity block, SaveCallback callback);
  */

}
