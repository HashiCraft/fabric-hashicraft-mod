package com.hashicorp.hashicraft.item;

import com.hashicorp.hashicraft.entity.AppMinecartEntity;
import com.hashicorp.hashicraft.entity.ModEntities;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.enums.RailShape;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

import java.util.List;

public class AppMinecart extends Item {

    private static final DispenserBehavior DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {

        private final ItemDispenserBehavior defaultBehavior = new ItemDispenserBehavior();

        @Override
        public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            double g;
            Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
            ServerWorld world = pointer.getWorld();
            double d = pointer.getX() + (double) direction.getOffsetX() * 1.125;
            double e = Math.floor(pointer.getY()) + (double) direction.getOffsetY();
            double f = pointer.getZ() + (double) direction.getOffsetZ() * 1.125;
            BlockPos blockPos = pointer.getPos().offset(direction);
            BlockState blockState = world.getBlockState(blockPos);
            RailShape railShape = blockState.getBlock() instanceof AbstractRailBlock
                    ? blockState.get(((AbstractRailBlock) blockState.getBlock()).getShapeProperty())
                    : RailShape.NORTH_SOUTH;
            if (blockState.isIn(BlockTags.RAILS)) {
                g = railShape.isAscending() ? 0.6 : 0.1;
            } else if (blockState.isAir() && world.getBlockState(blockPos.down()).isIn(BlockTags.RAILS)) {
                BlockState blockState2 = world.getBlockState(blockPos.down());
                RailShape railShape22 = blockState2.getBlock() instanceof AbstractRailBlock
                        ? blockState2.get(((AbstractRailBlock) blockState2.getBlock()).getShapeProperty())
                        : RailShape.NORTH_SOUTH;
                g = direction == Direction.DOWN || !railShape22.isAscending() ? -0.9 : -0.4;
            } else {
                return this.defaultBehavior.dispense(pointer, stack);
            }
            AppMinecartEntity entity = new AppMinecartEntity(ModEntities.APP_MINECART, world);
            entity.setPos(d, e + g, f);
            if (stack.hasCustomName()) {
                entity.setCustomName(stack.getName());
            }
            world.spawnEntity(entity);
            stack.decrement(1);
            return stack;
        }

        @Override
        protected void playSound(BlockPointer pointer) {
            pointer.getWorld().syncWorldEvent(WorldEvents.DISPENSER_DISPENSES, pointer.getPos(), 0);
        }
    };

    public AppMinecart(Item.Settings settings) {
        super(settings);
        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos;
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());
        if (!blockState.isIn(BlockTags.RAILS)) {
            return ActionResult.FAIL;
        }
        ItemStack itemStack = context.getStack();
        if (!world.isClient) {
            RailShape railShape = blockState.getBlock() instanceof AbstractRailBlock
                    ? blockState.get(((AbstractRailBlock) blockState.getBlock()).getShapeProperty())
                    : RailShape.NORTH_SOUTH;
            double d = 0.0;
            if (railShape.isAscending()) {
                d = 0.5;
            }
            AppMinecartEntity entity = new AppMinecartEntity(ModEntities.APP_MINECART, world);
            entity.setPos((double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.0625 + d,
                    (double) blockPos.getZ() + 0.5);
            if (itemStack.hasCustomName()) {
                entity.setCustomName(itemStack.getName());
            }
            world.spawnEntity(entity);
            world.emitGameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
        }
        itemStack.decrement(1);
        return ActionResult.success(world.isClient);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (!itemStack.hasNbt()) {
            tooltip.add(Text.literal("Not valid").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            return;
        }

        CartNbtData data = CartNbtData.getCustomNbt(itemStack);

        tooltip.add(Text.literal("Name").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
        tooltip.add(Text.literal(data.getName()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(Text.literal(""));

        tooltip.add(Text.literal("Version").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
        tooltip.add(Text.literal(data.getVersion()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(Text.literal(""));

        tooltip.add(Text.literal("Nomad Deployment").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
        tooltip.add(Text.literal(data.getNomadDeployment()).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        tooltip.add(Text.literal(""));
    }

    @Override
    public Text getName() {
        return Text.translatable(this.getTranslationKey());
    }

    @Override
    public Text getName(ItemStack stack) {
        CartNbtData data = CartNbtData.getCustomNbt(stack);
        String name = data.getName();
        String version = data.getVersion();

        MutableText text = Text.translatable(this.getTranslationKey());

        if (name != "" && version != "") {
            text.append(
                    Text.literal(" (" + name + ":" + version + ")").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        }

        return text;
    }
}
