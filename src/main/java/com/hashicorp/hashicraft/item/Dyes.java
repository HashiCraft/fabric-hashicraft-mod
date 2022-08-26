package com.hashicorp.hashicraft.item;

import com.hashicorp.hashicraft.recipe.Ingredient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dyes {
    public static final Item DEFAULT_COLOR = Items.BLACK_DYE;

    public static final Map<String, Item> COLORS = new HashMap<>() {{
        put("white_dye", Items.WHITE_DYE);

        put("orange_dye",Items.ORANGE_DYE);

        put("magenta_dye",Items.MAGENTA_DYE);

        put("light_blue_dye",Items.LIGHT_BLUE_DYE);

        put("yellow_dye",Items.YELLOW_DYE);

        put("lime_dye",Items.LIME_DYE);

        put("pink_dye",Items.PINK_DYE);

        put("gray_dye",Items.GRAY_DYE);

        put("light_gray_dye",Items.LIGHT_GRAY_DYE);

        put("cyan_dye",Items.CYAN_DYE);

        put("purple_dye",Items.PURPLE_DYE);

        put("blue_dye",Items.BLUE_DYE);

        put("brown_dye",Items.BROWN_DYE);

        put("green_dye",Items.GREEN_DYE);

        put("red_dye",Items.RED_DYE);

        put("black_dye",Items.BLACK_DYE);
    }};

    public static List<Ingredient> getAllDyesAsIngredients() {
        List<Ingredient> dyes = new ArrayList<>();
        for (String dye : COLORS.keySet()) {
            dyes.add(new Ingredient(new Identifier("minecraft:" + dye)));
        }
        return dyes;
    }
}
