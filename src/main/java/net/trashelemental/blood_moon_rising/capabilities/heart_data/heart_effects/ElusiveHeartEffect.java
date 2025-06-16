package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.junkyard_lib.util.UtilMethods;

import java.util.UUID;

/**
 * Increases player speed and jump height, and adds a small chance to dodge incoming damage.
 */
public class ElusiveHeartEffect extends AbstractHeartEffect {

    public ElusiveHeartEffect() {
        super (HEALTH_REDUCTION_UUID, "Elusive", -2.0);
    }

    private static final UUID SPEED_BONUS_UUID = UUID.fromString("9b4c2b1c-cf84-4f20-bf18-e063b14c12ea");
    private static final UUID HEALTH_REDUCTION_UUID = UUID.fromString("9b4c2b1c-cf84-4f20-bf18-e063b14c12eb");

    @Override
    public void onTick(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.JUMP, 60, 0, false, false));
    }

    @Override
    public void onAdded(Player player) {
        UtilMethods.applyModifier(player, Attributes.MOVEMENT_SPEED, getSpeedModifier());

        super.onAdded(player);
    }

    @Override
    public void onRemoved(Player player) {
        UtilMethods.removeModifier(player, Attributes.MOVEMENT_SPEED, SPEED_BONUS_UUID);

        super.onRemoved(player);
    }


    private AttributeModifier getSpeedModifier() {
        return new AttributeModifier(SPEED_BONUS_UUID, "Speed Bonus", 0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
