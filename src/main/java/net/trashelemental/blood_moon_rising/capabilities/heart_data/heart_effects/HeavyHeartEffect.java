package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.junkyard_lib.util.UtilMethods;

import java.util.UUID;

/**
 * Adds some innate armor and armor toughness at the cost of a speed penalty.
 */
public class HeavyHeartEffect extends AbstractHeartEffect {

    public HeavyHeartEffect() {
        super (HEALTH_REDUCTION_UUID, "Heavy", -2.0);
    }

    private static final UUID SPEED_REDUCTION_UUID = UUID.fromString("9a4c2b1c-cf84-4f20-bf18-e063b14c12ea");
    private static final UUID ARMOR_BONUS_UUID = UUID.fromString("9a4c2b1c-cf84-4f20-bf18-e063b14c12ec");
    private static final UUID TOUGHNESS_BONUS_UUID = UUID.fromString("9a4c2b1c-cf84-4f20-bf18-e063b14c12ed");
    private static final UUID HEALTH_REDUCTION_UUID = UUID.fromString("9a4c2b1c-cf84-4f20-bf18-e063b14c12eb");

    @Override
    public void onAdded(Player player) {
        UtilMethods.applyModifier(player, Attributes.MOVEMENT_SPEED, getSpeedModifier());
        UtilMethods.applyModifier(player, Attributes.ARMOR, getArmorModifier());
        UtilMethods.applyModifier(player, Attributes.ARMOR_TOUGHNESS, getToughnessModifier());

        super.onAdded(player);
    }

    @Override
    public void onRemoved(Player player) {
        UtilMethods.removeModifier(player, Attributes.MOVEMENT_SPEED, SPEED_REDUCTION_UUID);
        UtilMethods.removeModifier(player, Attributes.ARMOR, ARMOR_BONUS_UUID);
        UtilMethods.removeModifier(player, Attributes.ARMOR_TOUGHNESS, TOUGHNESS_BONUS_UUID);

        super.onRemoved(player);
    }


    private AttributeModifier getSpeedModifier() {
        return new AttributeModifier(SPEED_REDUCTION_UUID, "Speed Reduction", -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    private AttributeModifier getArmorModifier() {
        return new AttributeModifier(ARMOR_BONUS_UUID, "Armor Bonus", 4, AttributeModifier.Operation.ADDITION);
    }

    private AttributeModifier getToughnessModifier() {
        return new AttributeModifier(TOUGHNESS_BONUS_UUID, "Toughness Bonus", 1, AttributeModifier.Operation.ADDITION);
    }
}
