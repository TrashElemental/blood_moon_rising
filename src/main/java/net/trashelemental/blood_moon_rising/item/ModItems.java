package net.trashelemental.blood_moon_rising.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.armor.ModArmorMaterials;
import net.trashelemental.blood_moon_rising.armor.custom.VisceralArmorItem;
import net.trashelemental.blood_moon_rising.entity.ModEntities;
import net.trashelemental.blood_moon_rising.item.custom.HeartItem;
import net.trashelemental.blood_moon_rising.item.custom.consumables.*;
import net.trashelemental.blood_moon_rising.item.custom.consumables.foods.*;
import net.trashelemental.blood_moon_rising.item.custom.tools.*;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BloodMoonRising.MOD_ID);


    //Crafting Items
    public static final DeferredItem<Item> HEART = ITEMS.register("heart",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CHYME = ITEMS.register("chyme",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EXALTED_BONE = ITEMS.register("exalted_bone",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ICHOR_CLOT = ITEMS.register("ichor_clot",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LIGAMENT = ITEMS.register("ligament",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> EMBRYO = ITEMS.register("embryo",
            () -> new Item(new Item.Properties()));


    //Food Items
    public static final DeferredItem<Item> COOKED_HEART = ITEMS.register("cooked_heart",
            () -> new CookedHeartItem(new Item.Properties().food(ModFoods.COOKED_HEART)));
    public static final DeferredItem<Item> CURED_HEART = ITEMS.register("cured_heart",
            () -> new CuredHeartItem(new Item.Properties().food(ModFoods.CURED_HEART)));
    public static final DeferredItem<Item> HEART_OF_GOLD = ITEMS.register("heart_of_gold",
            () -> new HeartOfGoldItem(new Item.Properties().food(ModFoods.HEART_OF_GOLD)));
    public static final DeferredItem<Item> CONSECRATED_FLESH = ITEMS.register("consecrated_flesh",
            () -> new ConsecratedFleshItem(new Item.Properties().food(ModFoods.CONSECRATED_FLESH)));
    public static final DeferredItem<Item> COOKED_FLESH = ITEMS.register("cooked_flesh",
            () -> new CookedFleshItem(new Item.Properties().food(ModFoods.COOKED_FLESH)));
    public static final DeferredItem<Item> CURED_FLESH = ITEMS.register("cured_flesh",
            () -> new CuredFleshItem(new Item.Properties().food(ModFoods.CURED_FLESH)));
    public static final DeferredItem<Item> EXALTED_FLESH = ITEMS.register("exalted_flesh",
            () -> new ExaltedFleshItem(new Item.Properties().food(ModFoods.EXALTED_FLESH)));
    public static final DeferredItem<Item> JERKY = ITEMS.register("jerky",
            () -> new Item(new Item.Properties().food(ModFoods.JERKY)));


    //Functional Items
    public static final DeferredItem<Item> BOLUS = ITEMS.register("bolus",
            () -> new BolusItem(new Item.Properties()));
    public static final DeferredItem<Item> BOTTLE_OF_CHRISM = ITEMS.register("bottle_of_chrism",
            () -> new BottleOfChrismItem(new Item.Properties()));
    public static final DeferredItem<Item> BOTTLE_OF_ICHOR = ITEMS.register("bottle_of_ichor",
            () -> new BottleOfIchorItem(new Item.Properties()));
    public static final DeferredItem<Item> PARASITE_EGGS = ITEMS.register("parasite_eggs",
            () -> new ParasiteEggsItem(new Item.Properties()));
    public static final DeferredItem<Item> MULTIPLYING_MORSEL = ITEMS.register("multiplying_morsel",
            () -> new MultiplyingMorselItem(new Item.Properties()));
    public static final DeferredItem<Item> AMNION = ITEMS.register("amnion",
            () -> new Item(new Item.Properties()));


    //Not Implemented Yet
//  public static final DeferredItem<Item> FLESHGROWTH_SEED = ITEMS.register("fleshgrowth_seed",
//          () -> new Item(new Item.Properties()));


    //Armor Items
    public static final DeferredItem<Item> VISCERAL_HELMET = ITEMS.register("visceral_helmet",
            () -> new VisceralArmorItem(ModArmorMaterials.VISCERAL, ArmorItem.Type.HELMET, new Item.Properties()
                    .stacksTo(1).durability(363)));
    public static final DeferredItem<Item> VISCERAL_CHESTPLATE = ITEMS.register("visceral_chestplate",
            () -> new VisceralArmorItem(ModArmorMaterials.VISCERAL, ArmorItem.Type.CHESTPLATE, new Item.Properties()
                    .stacksTo(1).durability(528)));
    public static final DeferredItem<Item> VISCERAL_LEGGINGS = ITEMS.register("visceral_leggings",
            () -> new VisceralArmorItem(ModArmorMaterials.VISCERAL, ArmorItem.Type.LEGGINGS, new Item.Properties()
                    .stacksTo(1).durability(495)));
    public static final DeferredItem<Item> VISCERAL_BOOTS = ITEMS.register("visceral_boots",
            () -> new VisceralArmorItem(ModArmorMaterials.VISCERAL, ArmorItem.Type.BOOTS, new Item.Properties()
                    .stacksTo(1).durability(429)));



    //Tools
    public static final DeferredItem<Item> FLEAM = ITEMS.register("fleam",
            () -> new FleamItem(new Item.Properties().stacksTo(1).durability(35)));
    public static final DeferredItem<Item> AUGUR = ITEMS.register("augur",
            () -> new AugurItem(new Item.Properties()));
    public static final DeferredItem<Item> SANGUINE_CHALICE = ITEMS.register("sanguine_chalice",
            () -> new SanguineChaliceItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<AxeItem> BUTCHERS_CLEAVER = ITEMS.register("butchers_cleaver",
        () -> new ButchersCleaverItem(ModToolTiers.BMR, new Item.Properties()
                .attributes(AxeItem.createAttributes(ModToolTiers.BMR, 5, -3.2f))));
    public static final DeferredItem<SwordItem> JAWBLADE = ITEMS.register("jawblade",
            () -> new JawbladeItem(ModToolTiers.BMR, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.BMR, 3, -2.4f))));
    public static final DeferredItem<SwordItem> SACRIFICIAL_DAGGER = ITEMS.register("sacrificial_dagger",
            () -> new SacrificialDaggerItem(ModToolTiers.BMR, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModToolTiers.BMR, 1, -1.5f))));
    public static final DeferredItem<AxeItem> WARHAM = ITEMS.register("warham",
            () -> new WarhamItem(ModToolTiers.BMR, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModToolTiers.BMR, 5, -3.2f))));
    public static final DeferredItem<Item> SACRED_SPEAR = ITEMS.register("sacred_spear",
            () -> new SacredSpearItem(new Item.Properties().stacksTo(1).durability(1360)
                    .attributes(SacredSpearItem.createAttributes())
                    .component(DataComponents.TOOL, SacredSpearItem.createToolProperties())));
    public static final DeferredItem<Item> BLOODSHOT = ITEMS.register("bloodshot",
            () -> new Item(new Item.Properties()));




    //Spawn Eggs
    public static final Supplier<DeferredSpawnEggItem> LEECH_SPAWN_EGG = ITEMS.register("leech_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.LEECH, -10092544, -3407872, new Item.Properties()));
    public static final Supplier<DeferredSpawnEggItem> MOSQUITO_SPAWN_EGG = ITEMS.register("mosquito_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.MOSQUITO, -13096680, -3407872, new Item.Properties()));


    //Hearts
    public static final DeferredItem<Item> ASTRAL_HEART = ITEMS.register("astral_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> BROKEN_HEART = ITEMS.register("broken_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> DIVIDING_HEART = ITEMS.register("dividing_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> ECHOING_HEART = ITEMS.register("echoing_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> ELUSIVE_HEART = ITEMS.register("elusive_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> FERAL_HEART = ITEMS.register("feral_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> FRANTIC_HEART = ITEMS.register("frantic_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> FROZEN_HEART = ITEMS.register("frozen_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> HEAVY_HEART = ITEMS.register("heavy_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> HUNGRY_HEART = ITEMS.register("hungry_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> SCORCHED_HEART = ITEMS.register("scorched_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> SELFLESS_HEART = ITEMS.register("selfless_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> SPITEFUL_HEART = ITEMS.register("spiteful_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> TAINTED_HEART = ITEMS.register("tainted_heart",
            () -> new HeartItem(new Item.Properties()));
    public static final DeferredItem<Item> WRATHFUL_HEART = ITEMS.register("wrathful_heart",
            () -> new HeartItem(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
