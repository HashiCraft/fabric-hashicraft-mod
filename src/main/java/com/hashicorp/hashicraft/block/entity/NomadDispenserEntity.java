package com.hashicorp.hashicraft.block.entity;

import com.github.hashicraft.stateful.blocks.StatefulBlockEntity;
import com.github.hashicraft.stateful.blocks.Syncable;
import com.google.common.collect.Lists;
import com.hashicorp.hashicraft.item.CartNbtData;
import com.hashicorp.hashicraft.item.ModItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hashicorp.hashicraft.item.CartNbtData.DEFAULT_APPLICATION;
import static com.hashicorp.hashicraft.item.CartNbtData.DEFAULT_NOMAD_DEPLOYMENT;
import static com.hashicorp.hashicraft.item.Dyes.COLORS;
import static com.hashicorp.hashicraft.item.Dyes.DEFAULT_COLOR;

public class NomadDispenserEntity extends StatefulBlockEntity {

    @Syncable
    private String name = DEFAULT_APPLICATION;

    @Syncable
    private String color = "blue";

    @Syncable
    private String version = color;

    @Syncable
    private String nomadDeployment = DEFAULT_NOMAD_DEPLOYMENT;

    @Syncable
    private List<String> colors = Lists.newArrayList(color);

    public NomadDispenserEntity(BlockPos pos, BlockState state) {
        super(BlockEntities.NOMAD_DISPENSER_ENTITY, pos, state, null);
    }

    public NomadDispenserEntity(BlockPos pos, BlockState state, Block parent) {
        super(BlockEntities.NOMAD_DISPENSER_ENTITY, pos, state, parent);
    }

    public void dispense(Level level, BlockSource pointer, ItemStack stack, int offset, Direction side) {
        BlockPos pos = pointer.pos();
        double x = pos.getX() + 0.7D * (double) side.getStepX();
        double y = pos.getY() + 0.7D * (double) side.getStepY();
        double z = pos.getZ() + 0.7D * (double) side.getStepZ();

        if (side.getAxis() == Direction.Axis.Y) {
            y -= 0.125D;
        } else {
            y -= 0.15625D;
        }

        ItemEntity entity = new ItemEntity(level, x, y, z, stack);
        double g = level.getRandom().nextDouble() * 0.1D + 0.2D;
        entity.setDeltaMovement(
                level.getRandom().nextGaussian() * 0.007499999832361937D * (double) offset + (double) side.getStepX() * g,
                level.getRandom().nextGaussian() * 0.007499999832361937D * (double) offset + 0.20000000298023224D,
                level.getRandom().nextGaussian() * 0.007499999832361937D * (double) offset + (double) side.getStepZ() * g);
        level.addFreshEntity(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.markForUpdate();
    }

    public String getNomadDeployment() {
        return nomadDeployment;
    }

    public void setNomadDeployment(String nomadDeployment) {
        this.nomadDeployment = nomadDeployment;
        this.markForUpdate();
    }

    public String getVersion() {
        return version;
    }

    public String getColors() {
        return String.join(",", this.colors);
    }

    public void setColorAndVersion(String color, String version) {
        this.color = color;
        this.version = version;
        this.markForUpdate();
    }

    public void setColors(String colorText) {
        this.colors = Stream
                .of(colorText.split(",", -1))
                .collect(Collectors.toList());
        setColorAndVersion(this.colors.get(0), this.colors.get(0));
    }

    public ItemStack getApplication() {
        ItemStack application = new ItemStack(ModItems.APPLICATION_ITEM);

        CartNbtData data = new CartNbtData(this.getName(), this.getVersion(), this.getNomadDeployment());
        data.setCustomNbt(application);

        return application;
    }

    public ItemStack getDye() {
        String dyeColor = this.color.replace(' ', '_') + "_dye";
        boolean isColorPresent = COLORS.containsKey(dyeColor);
        if (isColorPresent) {
            return new ItemStack(COLORS.get(dyeColor));
        }
        return new ItemStack(DEFAULT_COLOR);
    }

    public ItemStack getMinecart() {
        return new ItemStack(Items.MINECART);
    }

    public void changeColors() {
        String color = this.colors.get(0);
        int i = this.colors.indexOf(this.color);
        if (i + 1 != this.colors.size()) {
            color = this.colors.get(i + 1);
        }
        setColorAndVersion(color, color);
    }
}
