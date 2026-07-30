package com.hashicorp.hashicraft.item;

import com.hashicorp.hashicraft.entity.AppMinecartEntity;
import com.hashicorp.hashicraft.entity.ModEntities;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import java.util.function.Consumer;
import net.minecraft.world.item.component.TooltipDisplay;


import java.util.List;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.ChatFormatting;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.InteractionResult;

public class AppMinecart extends Item {

    private static final DispenseItemBehavior DISPENSER_BEHAVIOR = new DefaultDispenseItemBehavior() {

        private final DefaultDispenseItemBehavior defaultBehavior = new DefaultDispenseItemBehavior();

        @Override
        protected ItemStack execute(BlockSource pointer, ItemStack stack) {
            double g;
            Direction direction = pointer.state().getValue(DispenserBlock.FACING);
            ServerLevel level = pointer.level();
            double d = pointer.pos().getX() + (double) direction.getStepX() * 1.125;
            double e = Math.floor(pointer.pos().getY()) + (double) direction.getStepY();
            double f = pointer.pos().getZ() + (double) direction.getStepZ() * 1.125;
            BlockPos blockPos = pointer.pos().relative(direction);
            BlockState blockState = level.getBlockState(blockPos);
            RailShape railShape = blockState.getBlock() instanceof BaseRailBlock
                    ? blockState.getValue(((BaseRailBlock) blockState.getBlock()).getShapeProperty())
                    : RailShape.NORTH_SOUTH;
            if (blockState.is(BlockTags.RAILS)) {
                g = railShape.isSlope() ? 0.6 : 0.1;
            } else if (blockState.isAir() && level.getBlockState(blockPos.below()).is(BlockTags.RAILS)) {
                BlockState blockState2 = level.getBlockState(blockPos.below());
                RailShape railShape22 = blockState2.getBlock() instanceof BaseRailBlock
                        ? blockState2.getValue(((BaseRailBlock) blockState2.getBlock()).getShapeProperty())
                        : RailShape.NORTH_SOUTH;
                g = direction == Direction.DOWN || !railShape22.isSlope() ? -0.9 : -0.4;
            } else {
                return this.defaultBehavior.dispense(pointer, stack);
            }
            AppMinecartEntity entity = new AppMinecartEntity(ModEntities.APP_MINECART, level);
            entity.setPos(d, e + g, f);
            if (stack.has(DataComponents.CUSTOM_NAME)) {
                entity.setCustomName(stack.getHoverName());
            }
            level.addFreshEntity(entity);
            stack.shrink(1);
            return stack;
        }

        @Override
        protected void playSound(BlockSource pointer) {
            pointer.level().levelEvent(null, 1000, pointer.pos(), 0);
        }
    };

    public AppMinecart(Item.Properties settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos blockPos;
        Level level = context.getLevel();
        BlockState blockState = level.getBlockState(blockPos = context.getClickedPos());
        if (!blockState.is(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        }
        ItemStack itemStack = context.getItemInHand();
        if (!level.isClientSide()) {
            RailShape railShape = blockState.getBlock() instanceof BaseRailBlock
                    ? blockState.getValue(((BaseRailBlock) blockState.getBlock()).getShapeProperty())
                    : RailShape.NORTH_SOUTH;
            double d = 0.0;
            if (railShape.isSlope()) {
                d = 0.5;
            }
            AppMinecartEntity entity = new AppMinecartEntity(ModEntities.APP_MINECART, level);
            entity.setPos((double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.0625 + d,
                    (double) blockPos.getZ() + 0.5);
            if (itemStack.has(DataComponents.CUSTOM_NAME)) {
                entity.setCustomName(itemStack.getHoverName());
            }
            level.addFreshEntity(entity);
            level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
        }
        itemStack.shrink(1);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, java.util.function.Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        if (!itemStack.has(DataComponents.CUSTOM_DATA)) {
            tooltip.accept(Component.literal("Not valid").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
            return;
        }

        CartNbtData data = CartNbtData.getCustomNbt(itemStack);

        tooltip.accept(Component.literal("Name").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
        tooltip.accept(Component.literal(data.getName()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        tooltip.accept(Component.literal(""));

        tooltip.accept(Component.literal("Version").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
        tooltip.accept(Component.literal(data.getVersion()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        tooltip.accept(Component.literal(""));

        tooltip.accept(Component.literal("Nomad Deployment").setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
        tooltip.accept(Component.literal(data.getNomadDeployment()).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        tooltip.accept(Component.literal(""));
    }

    public Component getName() {
        return Component.translatable(this.getDescriptionId());
    }

    @Override
    public Component getName(ItemStack stack) {
        CartNbtData data = CartNbtData.getCustomNbt(stack);
        String name = data.getName();
        String version = data.getVersion();

        MutableComponent component = Component.translatable(this.getDescriptionId());

        if (name != "" && version != "") {
            component.append(
                    Component.literal(" (" + name + ":" + version + ")").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }

        return component;
    }
}
