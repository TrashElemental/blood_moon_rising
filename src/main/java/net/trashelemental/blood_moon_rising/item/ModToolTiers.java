package net.trashelemental.blood_moon_rising.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

import java.util.List;

public class ModToolTiers {

    public static final Tier BMR = TierSortingRegistry.registerTier(

            new ForgeTier(
                    2,
                    1360,
                    8.0f,
                    3.0f,
                    15,
                    Tags.Blocks.NEEDS_WOOD_TOOL,
                    () -> Ingredient.of(ModItems.EXALTED_BONE.get())),
            new ResourceLocation(BloodMoonRising.MOD_ID, "bmr"),
            List.of(Tiers.IRON), List.of()
    );

}
