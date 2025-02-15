package net.trashelemental.blood_moon_rising.item;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.components.ModComponents;

public class ModItemProperties {

    public static void addCustomItemProperties() {

        ItemProperties.register(ModItems.JAWBLADE.get(), ResourceLocation.fromNamespaceAndPath(BloodMoonRising.MOD_ID, "stage"),
                (itemStack, clientLevel, livingEntity, i) -> {

                    Integer currentPoints = itemStack.get(ModComponents.POINTS);

                    if (currentPoints == null) {
                        return 0f;
                    } else if (currentPoints >= 16) {
                        return 2f;
                    } else if (currentPoints >= 8) {
                        return 1f;
                    }
                    return 0f;
                });

        ItemProperties.register(ModItems.BUTCHERS_CLEAVER.get(), ResourceLocation.fromNamespaceAndPath(BloodMoonRising.MOD_ID, "stage"),
                (itemStack, clientLevel, livingEntity, i) -> {

                    Integer currentPoints = itemStack.get(ModComponents.POINTS);

                    if (currentPoints == null) {
                        return 0f;
                    } else if (currentPoints >= 12) {
                        return 2f;
                    } else if (currentPoints >= 6) {
                        return 1f;
                    }
                    return 0f;
                });

        ItemProperties.register(ModItems.SACRIFICIAL_DAGGER.get(), ResourceLocation.fromNamespaceAndPath(BloodMoonRising.MOD_ID, "stage"),
                (itemStack, clientLevel, livingEntity, i) -> {

                    Integer currentPoints = itemStack.get(ModComponents.POINTS);

                    if (currentPoints == null) {
                        return 0f;
                    } else if (currentPoints >= 10) {
                        return 2f;
                    } else if (currentPoints >= 1) {
                        return 1f;
                    }
                    return 0f;
                });

        ItemProperties.register(ModItems.SANGUINE_CHALICE.get(), ResourceLocation.fromNamespaceAndPath(BloodMoonRising.MOD_ID, "stage"),
                (itemStack, clientLevel, livingEntity, i) -> {

                    Integer currentPoints = itemStack.get(ModComponents.POINTS);

                    if (currentPoints == null) {
                        return 0f;
                    } else if (currentPoints >= 12) {
                        return 1f;
                    }
                    return 0f;
                });


    }
}
