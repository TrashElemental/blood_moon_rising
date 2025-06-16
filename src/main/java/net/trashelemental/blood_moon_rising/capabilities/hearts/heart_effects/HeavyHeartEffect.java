package net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.junkyard_lib.util.UtilMethods;

import java.util.UUID;

/**
 * Adds some innate armor and armor toughness at the cost of a speed penalty.
 */
public class HeavyHeartEffect extends AbstractHeartEffect {

    public HeavyHeartEffect() {
        super ("heavy", -2);
    }

    @Override
    public void onAdded(Player player) {
        if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(getSpeedModName())) {
            player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(getSpeedModifier());
        }
        if (!player.getAttribute(Attributes.ARMOR).hasModifier(getArmorModName())) {
            player.getAttribute(Attributes.ARMOR).addPermanentModifier(getArmorModifier());
        }
        if (!player.getAttribute(Attributes.ARMOR_TOUGHNESS).hasModifier(getToughnessModName())) {
            player.getAttribute(Attributes.ARMOR_TOUGHNESS).addPermanentModifier(getToughnessModifier());
        }

        super.onAdded(player);
    }

    @Override
    public void onRemoved(Player player) {
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(getSpeedModifier());
        player.getAttribute(Attributes.ARMOR).removeModifier(getArmorModifier());
        player.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(getToughnessModifier());

        super.onRemoved(player);
    }


    private static final ResourceLocation getSpeedModName() {
        return BloodMoonRising.prefix("heavy_speed_decrease");
    }

    private AttributeModifier getSpeedModifier() {
        return new AttributeModifier(getSpeedModName(), -0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    private static final ResourceLocation getArmorModName() {
        return BloodMoonRising.prefix("heavy_armor_increase");
    }

    private AttributeModifier getArmorModifier() {
        return new AttributeModifier(getArmorModName(), 4.0, AttributeModifier.Operation.ADD_VALUE);
    }

    private static final ResourceLocation getToughnessModName() {
        return BloodMoonRising.prefix("heavy_toughness_increase");
    }

    private AttributeModifier getToughnessModifier() {
        return new AttributeModifier(getToughnessModName(), 1.0, AttributeModifier.Operation.ADD_VALUE);
    }
}
