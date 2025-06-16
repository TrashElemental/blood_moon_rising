package net.trashelemental.blood_moon_rising.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

public class ModItemProperties {

    public static void addCustomItemProperties() {

        ItemProperties.register(ModItems.JAWBLADE.get(), new ResourceLocation(BloodMoonRising.MOD_ID, "stage"),
                (itemStack, clientLevel, livingEntity, i) -> {

                    if (itemStack.hasTag() && itemStack.getTag().contains("Points")) {
                        int currentPoints = itemStack.getTag().getInt("Points");

                        if (currentPoints >= 16) {
                            return 2f;
                        } else if (currentPoints >= 8) {
                            return 1f;
                        }
                    }
                    return 0f;
                });

        ItemProperties.register(ModItems.BUTCHERS_CLEAVER.get(), new ResourceLocation(BloodMoonRising.MOD_ID, "stage"),
                (itemStack, clientLevel, livingEntity, i) -> {

                    if (itemStack.hasTag() && itemStack.getTag().contains("Points")) {
                        int currentPoints = itemStack.getTag().getInt("Points");

                        if (currentPoints >= 12) {
                            return 2f;
                        } else if (currentPoints >= 6) {
                            return 1f;
                        }
                    }
                    return 0f;
                });

        ItemProperties.register(ModItems.SACRIFICIAL_DAGGER.get(), new ResourceLocation(BloodMoonRising.MOD_ID, "stage"),
                (itemStack, clientLevel, livingEntity, i) -> {

                    if (itemStack.hasTag() && itemStack.getTag().contains("Points")) {
                        int currentPoints = itemStack.getTag().getInt("Points");

                        if (currentPoints >= 10) {
                            return 2f;
                        } else if (currentPoints >= 1) {
                            return 1f;
                        }
                    }
                    return 0f;
                });

        ItemProperties.register(ModItems.SACRED_SPEAR.get(), new ResourceLocation (BloodMoonRising.MOD_ID, "stage"),
                (itemStack, clientLevel, livingEntity, i) -> {

                    if (itemStack.hasTag() && itemStack.getTag().contains("Points")) {
                        int currentPoints = itemStack.getTag().getInt("Points");

                        if (currentPoints >= 1) {
                            return 1f;
                        }
                        return 0f;
                    }
                    return 0f;
                });

        ItemProperties.register(ModItems.SANGUINE_CHALICE.get(), new ResourceLocation (BloodMoonRising.MOD_ID, "stage"),
                (itemStack, clientLevel, livingEntity, i) -> {

                    if (itemStack.hasTag() && itemStack.getTag().contains("Points")) {
                        int currentPoints = itemStack.getTag().getInt("Points");

                    if (currentPoints >= 12) {
                        return 1f;
                    }
                    return 0f;
                }
                    return 0f;
                });


    }
}
