package net.trashelemental.blood_moon_rising.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.trashelemental.blood_moon_rising.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {

        //Items

        //Crafting Items



        //Food Items
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEART_OF_GOLD.get())
                .pattern("bbb")
                .pattern("bab")
                .pattern("bbb")
                .define('a', ModItems.HEART.get())
                .define('b', Items.GOLD_INGOT)
                .unlockedBy("has_heart", has(ModItems.HEART.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath("blood_moon_rising", "heart_of_gold"));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.HEART.get()),
                        RecipeCategory.FOOD,
                        ModItems.COOKED_HEART.get(),
                        0.35f,
                        200)
                .unlockedBy("has_heart", has(ModItems.HEART.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath("blood_moon_rising", "cooked_heart_smelting"));

        SimpleCookingRecipeBuilder.smoking(
                        Ingredient.of(ModItems.HEART.get()),
                        RecipeCategory.FOOD,
                        ModItems.COOKED_HEART.get(),
                        0.35f,
                        100)
                .unlockedBy("has_heart", has(ModItems.HEART.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath("blood_moon_rising", "cooked_heart_smoking"));

        SimpleCookingRecipeBuilder.campfireCooking(
                        Ingredient.of(ModItems.HEART.get()),
                        RecipeCategory.FOOD,
                        ModItems.COOKED_HEART.get(),
                        0.35f,
                        600)
                .unlockedBy("has_heart", has(ModItems.HEART.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath("blood_moon_rising", "cooked_heart_campfire"));

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.EXALTED_FLESH.get()),
                        RecipeCategory.FOOD,
                        ModItems.COOKED_FLESH.get(),
                        0.35f,
                        200)
                .unlockedBy("has_exalted_flesh", has(ModItems.EXALTED_FLESH.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath("blood_moon_rising", "cooked_flesh_smelting"));

        SimpleCookingRecipeBuilder.smoking(
                        Ingredient.of(ModItems.EXALTED_FLESH.get()),
                        RecipeCategory.FOOD,
                        ModItems.COOKED_FLESH.get(),
                        0.35f,
                        100)
                .unlockedBy("has_exalted_flesh", has(ModItems.EXALTED_FLESH.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath("blood_moon_rising", "cooked_flesh_smoking"));

        SimpleCookingRecipeBuilder.campfireCooking(
                        Ingredient.of(ModItems.EXALTED_FLESH.get()),
                        RecipeCategory.FOOD,
                        ModItems.COOKED_FLESH.get(),
                        0.35f,
                        600)
                .unlockedBy("has_exalted_flesh", has(ModItems.EXALTED_FLESH.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath("blood_moon_rising", "cooked_flesh_campfire"));



        //Armor Items





        //Blocks

        //Building Blocks



        //Functional Blocks



//Added Recipes for Vanilla Items

        
    }
}
