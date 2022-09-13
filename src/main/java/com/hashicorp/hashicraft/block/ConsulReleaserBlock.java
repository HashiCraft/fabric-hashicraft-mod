package com.hashicorp.hashicraft.block;

import com.github.hashicraft.stateful.blocks.StatefulBlock;
import com.hashicorp.hashicraft.block.entity.BlockEntities;
import com.hashicorp.hashicraft.block.entity.ConsulReleaserEntity;
import com.hashicorp.hashicraft.item.ModItems;
import com.hashicorp.hashicraft.ui.event.ConsulReleaserClicked;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ConsulReleaserBlock extends StatefulBlock {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty HEALTHY = BooleanProperty.of("healthy");

    public ConsulReleaserBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(HEALTHY, true));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ConsulReleaserEntity(pos, state);
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(FACING, HEALTHY);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
                              BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ConsulReleaserEntity releaser) {
            ItemStack stack = player.getStackInHand(hand);

            if (world.isClient) {
                if (stack.isOf(ModItems.WRENCH_ITEM)) {
                    ConsulReleaserClicked.EVENT.invoker().interact(releaser, releaser::markForUpdate);
                    if (releaser.createRelease()) {
                        player.sendMessage(Text.literal("INFO - Release created"), true);
                    } else {
                        player.sendMessage(Text.literal("ERROR - Release not created"), true);
                    }
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
                                                                  BlockEntityType<T> type) {
        return checkType(type, BlockEntities.CONSUL_RELEASER_ENTITY, ConsulReleaserEntity::tick);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(HEALTHY)) {
            for (int i = 0; i < 4; ++i) {
                double x = (double) pos.getX() + 0.2D + (double) random.nextInt(6) / 10;
                double y = (double) pos.getY() + 1.0D;
                double z = (double) pos.getZ() + 0.2D + (double) random.nextInt(6) / 10;
                double vx = 0;
                double vy = ((double) random.nextFloat()) * 0.1D;
                double vz = 0;
                world.addParticle(ParticleTypes.SMOKE, x, y, z, vx, vy, vz);
            }

            for (int i = 0; i < 2; ++i) {
                double x = (double) pos.getX() + 0.2D + (double) random.nextInt(6) / 10;
                double y = (double) pos.getY() + 1.0D;
                double z = (double) pos.getZ() + 0.2D + (double) random.nextInt(6) / 10;
                double vx = 0;
                double vy = ((double) random.nextFloat()) * 0.01D;
                double vz = 0;
                world.addParticle(ParticleTypes.FLAME, x, y, z, vx, vy, vz);
            }
        }
    }
}
