package net.trashelemental.blood_moon_rising.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class ModToolTiers {

    public static final Tier BMR = new SimpleTier(BlockTags.INCORRECT_FOR_WOODEN_TOOL,
            1360, 8.0F, 3F, 15, () -> Ingredient.of(ModItems.EXALTED_BONE));


}
