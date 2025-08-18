package net.trashelemental.blood_moon_rising.entity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.MolarEntity;
import net.trashelemental.blood_moon_rising.item.ModItems;
import net.trashelemental.blood_moon_rising.util.ModTags;

import java.util.List;


public class MolarEvents {

    public static void molarTrade(MolarEntity molar, ItemStack item) {
        if (!(molar.level() instanceof ServerLevel level)) return;

        molarTradeVFX(molar);

        if (item.is(Items.CHORUS_FRUIT)) {
            molarEatChorusFruit(molar);
            return;
        }

        RandomSource random = level.random;
        BlockPos pos = molar.blockPosition();
        Vec3 spawnPos = Vec3.atCenterOf(pos);
        FoodProperties food = item.getFoodProperties(molar);

        //Super Special
        if (item.is(ModTags.Items.MOLAR_SUPER_SPECIAL_INPUT)) {
            spawnRandomItemsFromTag(level, ModTags.Items.MOLAR_SUPER_SPECIAL_OUTPUT, 2, 4, spawnPos);
            spawnRandomAmountOfExperience(level, spawnPos, 20, 25);
        }

        //Special
        else if (item.is(ModTags.Items.MOLAR_SPECIAL_INPUT)) {
            if (random.nextFloat() < 0.8f) {
                spawnRandomItemsFromTag(level, ModTags.Items.MOLAR_SPECIAL_OUTPUT, 1, 2, spawnPos);
            }
            spawnRandomAmountOfExperience(level, spawnPos, 10, 15);
        }

        //Bone
        else if (item.is(ModTags.Items.MOLAR_BONE_INPUT)) {
            if (random.nextFloat() < 0.2f) {
                spawnRandomNumberOfItems(level, ModItems.BOLUS.get(), 1, 2, spawnPos);
            } else {
                spawnRandomItemsFromTag(level, ModTags.Items.MOLAR_BONE_OUTPUT, 3, 5, spawnPos);
            }

            if (random.nextFloat() < 0.8f) {
                spawnRandomAmountOfExperience(level, spawnPos, 5, 10);
            }
        }

        //Plant-based
        else if (item.is(ModTags.Items.MOLAR_PLANT_INPUT)) {
            if (random.nextFloat() < 0.4f) {
               spawnRandomItemsFromTag(level, ModTags.Items.MOLAR_PLANT_OUTPUT, 2, 3, spawnPos);
            }

            if (random.nextFloat() < 0.5f) {
                spawnRandomAmountOfExperience(level, spawnPos, 3, 5);
            }
        }

        //Meat
        else if (item.is(ModTags.Items.MOLAR_MEAT_INPUT) || (food != null && food.isMeat()))  {
            if (random.nextFloat() < 0.05f) {
                //Replace with Wound Tear when done
                spawnRandomNumberOfItems(level, ModItems.HEART.get(), 1, 2, spawnPos);
            } else {
                spawnRandomItemsFromTag(level, ModTags.Items.MOLAR_MEAT_OUTPUT, 2, 3, spawnPos);
            }

            if (random.nextFloat() < 0.75f) {
                spawnRandomAmountOfExperience(level, spawnPos, 5, 10);
            }
        }

        else {
            if (random.nextFloat() < 0.4f) {
                spawnRandomNumberOfItems(level, ModItems.CHYME.get(), 1, 2, spawnPos);
            }
            spawnRandomAmountOfExperience(level, spawnPos, 3, 5);
        }
    }

    public static void spawnRandomItemsFromTag(ServerLevel level, TagKey<Item> tag, int minTotalItems, int maxTotalItems, Vec3 pos) {
        Registry<Item> itemRegistry = level.registryAccess().registryOrThrow(Registries.ITEM);
        HolderSet.Named<Item> tagSet = itemRegistry.getTag(tag).orElse(null);

        if (tagSet == null || tagSet.size() == 0) {
            System.out.println("Tag " + tag.location() + " is empty or missing.");
            return;
        }

        List<Item> items = tagSet.stream().map(Holder::value).toList();
        int total = level.random.nextInt(maxTotalItems - minTotalItems + 1) + minTotalItems;

        BloodMoonRising.queueServerWork(50, () -> {
            for (int i = 0; i < total; i++) {
                Item randomItem = items.get(level.random.nextInt(items.size()));
                ItemStack stack = new ItemStack(randomItem, 1);
                level.addFreshEntity(new ItemEntity(level, pos.x, pos.y, pos.z, stack));
            }
        });
    }

    public static void spawnRandomNumberOfItems(ServerLevel level, Item item, int min, int max, Vec3 pos) {
        int amount = level.random.nextInt(max - min + 1) + min;
        ItemStack stack = new ItemStack(item, amount);

        BloodMoonRising.queueServerWork(50, () -> {
            level.addFreshEntity(new net.minecraft.world.entity.item.ItemEntity(level, pos.x, pos.y, pos.z, stack));
        });
    }

    public static void spawnRandomAmountOfExperience(ServerLevel level, Vec3 pos, int min, int max) {
        int amount = level.random.nextInt(max - min + 1) + min;

        BloodMoonRising.queueServerWork(50, () -> {
            ExperienceOrb.award(level, pos, amount);
        });
    }

    public static void molarTradeVFX(MolarEntity molar) {
        BloodMoonRising.queueServerWork(10, () -> {
            molar.playSound(SoundEvents.FOX_EAT, 1.0f, 0.6f);
        });
        BloodMoonRising.queueServerWork(20, () -> {
            molar.playSound(SoundEvents.FOX_EAT, 1.0f, 0.6f);
        });
        BloodMoonRising.queueServerWork(30, () -> {
            molar.playSound(SoundEvents.FOX_EAT, 1.0f, 0.6f);
        });
        BloodMoonRising.queueServerWork(50, () -> {
            molar.playSound(SoundEvents.FOX_SPIT, 2.0f, 0.6f);
        });
    }

    public static void molarEatChorusFruit(MolarEntity molar) {
        BloodMoonRising.queueServerWork(50, () -> {
            Level level = molar.level();
            if (!level.isClientSide) {
                double startX = molar.getX();
                double startY = molar.getY();
                double startZ = molar.getZ();

                for (int i = 0; i < 16; ++i) {
                    double targetX = startX + (molar.getRandom().nextDouble() - 0.5D) * 16.0D;
                    double targetY = Mth.clamp(
                            startY + (double)(molar.getRandom().nextInt(16) - 8),
                            level.getMinBuildHeight(),
                            level.getMinBuildHeight() + ((ServerLevel) level).getLogicalHeight() - 1
                    );
                    double targetZ = startZ + (molar.getRandom().nextDouble() - 0.5D) * 16.0D;

                    if (molar.isPassenger()) {
                        molar.stopRiding();
                    }

                    if (molar.randomTeleport(targetX, targetY, targetZ, true)) {
                        molar.playSound(SoundEvents.FOX_TELEPORT, 1.0F, 1.0F);
                        break;
                    }
                }
            }
        });
    }
}
