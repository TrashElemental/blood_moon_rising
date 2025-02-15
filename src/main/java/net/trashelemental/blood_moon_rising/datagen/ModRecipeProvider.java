package net.trashelemental.blood_moon_rising.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.trashelemental.blood_moon_rising.item.ModItems;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {


        //Food Items
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEART_OF_GOLD.get())
                .pattern("bbb")
                .pattern("bab")
                .pattern("bbb")
                .define('a', ModItems.HEART.get())
                .define('b', Items.GOLD_INGOT)
                .unlockedBy(getHasName(ModItems.HEART.get()), has(ModItems.HEART.get()))
                .save(pWriter);

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.HEART.get()),
                        RecipeCategory.FOOD,
                        ModItems.COOKED_HEART.get(),
                        0.35f,
                        200)
                .unlockedBy("has_heart", has(ModItems.HEART.get()))
                .save(pWriter, new ResourceLocation("blood_moon_rising", "cooked_heart_smelting"));

        SimpleCookingRecipeBuilder.smoking(
                        Ingredient.of(ModItems.HEART.get()),
                        RecipeCategory.FOOD,
                        ModItems.COOKED_HEART.get(),
                        0.35f,
                        100)
                .unlockedBy("has_heart", has(ModItems.HEART.get()))
                .save(pWriter, new ResourceLocation("blood_moon_rising", "cooked_heart_smoking"));

        SimpleCookingRecipeBuilder.campfireCooking(
                        Ingredient.of(ModItems.HEART.get()),
                        RecipeCategory.FOOD,
                        ModItems.COOKED_HEART.get(),
                        0.35f,
                        600)
                .unlockedBy("has_heart", has(ModItems.HEART.get()))
                .save(pWriter, new ResourceLocation("blood_moon_rising", "cooked_heart_campfire_cooking"));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.EXALTED_FLESH.get()),
                        RecipeCategory.FOOD,
                        ModItems.COOKED_FLESH.get(),
                        0.35f,
                        200)
                .unlockedBy("has_exalted_flesh", has(ModItems.EXALTED_FLESH.get()))
                .save(pWriter, new ResourceLocation("blood_moon_rising", "cooked_flesh_smelting"));

        SimpleCookingRecipeBuilder.smoking(
                        Ingredient.of(ModItems.EXALTED_FLESH.get()),
                        RecipeCategory.FOOD,
                        ModItems.COOKED_FLESH.get(),
                        0.35f,
                        100)
                .unlockedBy("has_exalted_flesh", has(ModItems.EXALTED_FLESH.get()))
                .save(pWriter, new ResourceLocation("blood_moon_rising", "cooked_flesh_smoking"));

        SimpleCookingRecipeBuilder.campfireCooking(
                        Ingredient.of(ModItems.EXALTED_FLESH.get()),
                        RecipeCategory.FOOD,
                        ModItems.COOKED_FLESH.get(),
                        0.35f,
                        600)
                .unlockedBy("has_exalted_flesh", has(ModItems.EXALTED_FLESH.get()))
                .save(pWriter, new ResourceLocation("blood_moon_rising", "cooked_flesh_campfire_cooking"));



    }

}



