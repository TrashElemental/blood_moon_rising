package net.trashelemental.blood_moon_rising.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.ModEntities;
import net.trashelemental.blood_moon_rising.item.custom.consumables.BottleOfChrismItem;
import net.trashelemental.blood_moon_rising.item.custom.consumables.BottleOfIchorItem;
import net.trashelemental.blood_moon_rising.item.custom.consumables.MultiplyingMorselItem;
import net.trashelemental.blood_moon_rising.item.custom.consumables.ParasiteEggsItem;
import net.trashelemental.blood_moon_rising.item.custom.consumables.foods.*;
import net.trashelemental.blood_moon_rising.item.custom.tools.*;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BloodMoonRising.MOD_ID);

    //Crafting Items
    public static final RegistryObject<Item> HEART = ITEMS.register("heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHYME = ITEMS.register("chyme",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EXALTED_BONE = ITEMS.register("exalted_bone",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ICHOR_CLOT = ITEMS.register("ichor_clot",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LIGAMENT = ITEMS.register("ligament",
            () -> new Item(new Item.Properties()));


    //Food Items
    public static final RegistryObject<Item> COOKED_HEART = ITEMS.register("cooked_heart",
            () -> new CookedHeartItem(new Item.Properties().food(ModFoods.COOKED_HEART)));
    public static final RegistryObject<Item> CURED_HEART = ITEMS.register("cured_heart",
            () -> new CuredHeartItem(new Item.Properties().food(ModFoods.CURED_HEART)));
    public static final RegistryObject<Item> HEART_OF_GOLD = ITEMS.register("heart_of_gold",
            () -> new HeartOfGoldItem(new Item.Properties().food(ModFoods.HEART_OF_GOLD)));
    public static final RegistryObject<Item> CONSECRATED_FLESH = ITEMS.register("consecrated_flesh",
            () -> new ConsecratedFleshItem(new Item.Properties().food(ModFoods.CONSECRATED_FLESH)));
    public static final RegistryObject<Item> COOKED_FLESH = ITEMS.register("cooked_flesh",
            () -> new CookedFleshItem(new Item.Properties().food(ModFoods.COOKED_FLESH)));
    public static final RegistryObject<Item> CURED_FLESH = ITEMS.register("cured_flesh",
            () -> new CuredFleshItem(new Item.Properties().food(ModFoods.CURED_FLESH)));
    public static final RegistryObject<Item> EXALTED_FLESH = ITEMS.register("exalted_flesh",
            () -> new ExaltedFleshItem(new Item.Properties().food(ModFoods.EXALTED_FLESH)));
    public static final RegistryObject<Item> JERKY = ITEMS.register("jerky",
            () -> new Item(new Item.Properties().food(ModFoods.JERKY)));


    //Functional Items
    public static final RegistryObject<Item> BOLUS = ITEMS.register("bolus",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BOTTLE_OF_CHRISM = ITEMS.register("bottle_of_chrism",
            () -> new BottleOfChrismItem(new Item.Properties()));
    public static final RegistryObject<Item> BOTTLE_OF_ICHOR = ITEMS.register("bottle_of_ichor",
            () -> new BottleOfIchorItem(new Item.Properties()));
    public static final RegistryObject<Item> PARASITE_EGGS = ITEMS.register("parasite_eggs",
            () -> new ParasiteEggsItem(new Item.Properties()));
    public static final RegistryObject<Item> MULTIPLYING_MORSEL = ITEMS.register("multiplying_morsel",
            () -> new MultiplyingMorselItem(new Item.Properties()));


    //Not Implemented Yet
//  public static final RegistryObject<Item> FLESHGROWTH_SEED = ITEMS.register("fleshgrowth_seed",
//          () -> new Item(new Item.Properties()));


    //Armor Items


    //Tools
    public static final RegistryObject<Item> FLEAM = ITEMS.register("fleam",
            () -> new FleamItem(new Item.Properties().stacksTo(1).durability(35)));
    public static final RegistryObject<Item> AUGUR = ITEMS.register("augur",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SANGUINE_CHALICE = ITEMS.register("sanguine_chalice",
            () -> new SanguineChaliceItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SACRED_SPEAR = ITEMS.register("sacred_spear",
            () -> new SacredSpearItem(new Item.Properties().stacksTo(1).durability(1360)));
    public static final RegistryObject<Item> BUTCHERS_CLEAVER = ITEMS.register("butchers_cleaver",
            () -> new ButchersCleaverItem(ModToolTiers.BMR, new Item.Properties()));
    public static final RegistryObject<Item> JAWBLADE = ITEMS.register("jawblade",
            () -> new JawbladeItem(ModToolTiers.BMR, new Item.Properties()));
    public static final RegistryObject<Item> SACRIFICIAL_DAGGER = ITEMS.register("sacrificial_dagger",
            () -> new SacrificialDaggerItem(ModToolTiers.BMR, new Item.Properties()));
    public static final RegistryObject<Item> WARHAM = ITEMS.register("warham",
            () -> new WarhamItem(ModToolTiers.BMR, new Item.Properties()));



    //Spawn Eggs
    public static final RegistryObject<Item> LEECH_SPAWN_EGG = ITEMS.register("leech_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.LEECH, -10092544, -3407872, new Item.Properties()));
    public static final RegistryObject<Item> MOSQUITO_SPAWN_EGG = ITEMS.register("mosquito_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.MOSQUITO, -13096680, -3407872, new Item.Properties()));


    //Hearts
    public static final RegistryObject<Item> ASTRAL_HEART = ITEMS.register("astral_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BROKEN_HEART = ITEMS.register("broken_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DIVIDING_HEART = ITEMS.register("dividing_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ECHOING_HEART = ITEMS.register("echoing_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ELUSIVE_HEART = ITEMS.register("elusive_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FERAL_HEART = ITEMS.register("feral_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FRANTIC_HEART = ITEMS.register("frantic_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FROZEN_HEART = ITEMS.register("frozen_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEAVY_HEART = ITEMS.register("heavy_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HUNGRY_HEART = ITEMS.register("hungry_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SCORCHED_HEART = ITEMS.register("scorched_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SELFLESS_HEART = ITEMS.register("selfless_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SPITEFUL_HEART = ITEMS.register("spiteful_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> TAINTED_HEART = ITEMS.register("tainted_heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WRATHFUL_HEART = ITEMS.register("wrathful_heart",
            () -> new Item(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
