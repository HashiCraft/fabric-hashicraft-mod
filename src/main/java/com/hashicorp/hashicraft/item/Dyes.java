package com.hashicorp.hashicraft.item;

import com.hashicorp.hashicraft.recipe.Ingredient;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.resources.Identifier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dyes {
    public static final Item DEFAULT_COLOR = Items.DYE.pick(DyeColor.BLACK);

    public static final Map<String, Item> COLORS = new HashMap<>() {{
        put("white_dye", Items.DYE.pick(DyeColor.WHITE));
        put("orange_dye", Items.DYE.pick(DyeColor.ORANGE));
        put("magenta_dye", Items.DYE.pick(DyeColor.MAGENTA));
        put("light_blue_dye", Items.DYE.pick(DyeColor.LIGHT_BLUE));
        put("yellow_dye", Items.DYE.pick(DyeColor.YELLOW));
        put("lime_dye", Items.DYE.pick(DyeColor.LIME));
        put("pink_dye", Items.DYE.pick(DyeColor.PINK));
        put("gray_dye", Items.DYE.pick(DyeColor.GRAY));
        put("light_gray_dye", Items.DYE.pick(DyeColor.LIGHT_GRAY));
        put("cyan_dye", Items.DYE.pick(DyeColor.CYAN));
        put("purple_dye", Items.DYE.pick(DyeColor.PURPLE));
        put("blue_dye", Items.DYE.pick(DyeColor.BLUE));
        put("brown_dye", Items.DYE.pick(DyeColor.BROWN));
        put("green_dye", Items.DYE.pick(DyeColor.GREEN));
        put("red_dye", Items.DYE.pick(DyeColor.RED));
        put("black_dye", Items.DYE.pick(DyeColor.BLACK));
    }};

    public static List<Ingredient> getAllDyesAsIngredients() {
        List<Ingredient> dyes = new ArrayList<>();
        for (String dye : COLORS.keySet()) {
            dyes.add(new Ingredient(Identifier.parse("minecraft:" + dye)));
        }
        return dyes;
    }
}