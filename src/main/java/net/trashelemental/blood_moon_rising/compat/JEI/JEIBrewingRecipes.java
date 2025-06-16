package net.trashelemental.blood_moon_rising.compat.JEI;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.trashelemental.blood_moon_rising.item.ModItems;
import net.trashelemental.blood_moon_rising.magic.brewing.ModPotions;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("deprecation")
@JeiPlugin
public class JEIBrewingRecipes implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("blood_moon_rising:brewing_recipes");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();

        List<IJeiBrewingRecipe> brewingRecipes = new ArrayList<>();
        ItemStack potion = new ItemStack(Items.POTION);
        ItemStack potion2 = new ItemStack(Items.POTION);
        List<ItemStack> ingredientStack = new ArrayList<>();
        List<ItemStack> inputStack = new ArrayList<>();


        //Health Boost Potion
        ingredientStack.add(new ItemStack(ModItems.HEART.get()));
        PotionUtils.setPotion(potion, Potions.AWKWARD);
        PotionUtils.setPotion(potion2, ModPotions.HEALTH_BOOST_POTION.get());
        brewingRecipes.add(factory.createBrewingRecipe(
                List.copyOf(ingredientStack),
                potion.copy(),
                potion2.copy()
        ));

        ingredientStack.clear();
        ingredientStack.add(new ItemStack(Items.REDSTONE));
        PotionUtils.setPotion(potion, ModPotions.HEALTH_BOOST_POTION.get());
        PotionUtils.setPotion(potion2, ModPotions.HEALTH_BOOST_POTION_LONG.get());
        brewingRecipes.add(factory.createBrewingRecipe(
                List.copyOf(ingredientStack),
                potion.copy(),
                potion2.copy()
        ));

        ingredientStack.clear();
        ingredientStack.add(new ItemStack(Items.GLOWSTONE_DUST));
        PotionUtils.setPotion(potion, ModPotions.HEALTH_BOOST_POTION.get());
        PotionUtils.setPotion(potion2, ModPotions.HEALTH_BOOST_POTION_STRONG.get());
        brewingRecipes.add(factory.createBrewingRecipe(
                List.copyOf(ingredientStack),
                potion.copy(),
                potion2.copy()
        ));


        //Absorption Potion
        ingredientStack.clear();
        ingredientStack.add(new ItemStack(ModItems.HEART_OF_GOLD.get()));
        PotionUtils.setPotion(potion, Potions.AWKWARD);
        PotionUtils.setPotion(potion2, ModPotions.ABSORPTION_POTION.get());
        brewingRecipes.add(factory.createBrewingRecipe(
                List.copyOf(ingredientStack),
                potion.copy(),
                potion2.copy()
        ));

        ingredientStack.clear();
        ingredientStack.add(new ItemStack(Items.REDSTONE));
        PotionUtils.setPotion(potion, ModPotions.ABSORPTION_POTION.get());
        PotionUtils.setPotion(potion2, ModPotions.ABSORPTION_POTION_LONG.get());
        brewingRecipes.add(factory.createBrewingRecipe(
                List.copyOf(ingredientStack),
                potion.copy(),
                potion2.copy()
        ));

        ingredientStack.clear();
        ingredientStack.add(new ItemStack(Items.GLOWSTONE_DUST));
        PotionUtils.setPotion(potion, ModPotions.ABSORPTION_POTION.get());
        PotionUtils.setPotion(potion2, ModPotions.ABSORPTION_POTION_STRONG.get());
        brewingRecipes.add(factory.createBrewingRecipe(
                List.copyOf(ingredientStack),
                potion.copy(),
                potion2.copy()
        ));


        //Health Down Potion
        ingredientStack.clear();
        ingredientStack.add(new ItemStack(Items.FERMENTED_SPIDER_EYE));
        PotionUtils.setPotion(potion, ModPotions.HEALTH_BOOST_POTION.get());
        PotionUtils.setPotion(potion2, ModPotions.HEALTH_DOWN_POTION.get());
        brewingRecipes.add(factory.createBrewingRecipe(
                List.copyOf(ingredientStack),
                potion.copy(),
                potion2.copy()
        ));

        ingredientStack.clear();
        ingredientStack.add(new ItemStack(Items.REDSTONE));
        PotionUtils.setPotion(potion, ModPotions.HEALTH_DOWN_POTION.get());
        PotionUtils.setPotion(potion2, ModPotions.HEALTH_DOWN_POTION_LONG.get());
        brewingRecipes.add(factory.createBrewingRecipe(
                List.copyOf(ingredientStack),
                potion.copy(),
                potion2.copy()
        ));

        ingredientStack.clear();
        ingredientStack.add(new ItemStack(Items.GLOWSTONE_DUST));
        PotionUtils.setPotion(potion, ModPotions.HEALTH_DOWN_POTION.get());
        PotionUtils.setPotion(potion2, ModPotions.HEALTH_DOWN_POTION_STRONG.get());
        brewingRecipes.add(factory.createBrewingRecipe(
                List.copyOf(ingredientStack),
                potion.copy(),
                potion2.copy()
        ));


        //Corrosion Potion
        ingredientStack.clear();
        ingredientStack.add(new ItemStack(ModItems.CHYME.get()));
        PotionUtils.setPotion(potion, Potions.AWKWARD);
        PotionUtils.setPotion(potion2, ModPotions.CORROSION_POTION.get());
        brewingRecipes.add(factory.createBrewingRecipe(
                List.copyOf(ingredientStack),
                potion.copy(),
                potion2.copy()
        ));

        ingredientStack.clear();
        ingredientStack.add(new ItemStack(Items.REDSTONE));
        PotionUtils.setPotion(potion, ModPotions.CORROSION_POTION.get());
        PotionUtils.setPotion(potion2, ModPotions.CORROSION_POTION_LONG.get());
        brewingRecipes.add(factory.createBrewingRecipe(
                List.copyOf(ingredientStack),
                potion.copy(),
                potion2.copy()
        ));

        ingredientStack.clear();
        ingredientStack.add(new ItemStack(Items.GLOWSTONE_DUST));
        PotionUtils.setPotion(potion, ModPotions.CORROSION_POTION.get());
        PotionUtils.setPotion(potion2, ModPotions.CORROSION_POTION_STRONG.get());
        brewingRecipes.add(factory.createBrewingRecipe(
                List.copyOf(ingredientStack),
                potion.copy(),
                potion2.copy()
        ));


        registration.addRecipes(RecipeTypes.BREWING, brewingRecipes);
    }
}
