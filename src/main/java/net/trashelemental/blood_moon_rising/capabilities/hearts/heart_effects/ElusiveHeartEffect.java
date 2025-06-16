package net.trashelemental.blood_moon_rising.capabilities.hearts.heart_effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.junkyard_lib.util.UtilMethods;

import java.util.UUID;

/**
 * Increases player speed and jump height, and adds a small chance to dodge incoming damage.
 */
public class ElusiveHeartEffect extends AbstractHeartEffect {

    public ElusiveHeartEffect() {
        super ("elusive", -2);
    }

    @Override
    public void onTick(Player player) {
        player.addEffect(new MobEffectInstance(MobEffects.JUMP, 60, 0, false, false));
    }

    @Override
    public void onAdded(Player player) {
        if (!player.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(getSpeedModName())) {
            player.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(getSpeedModifier());
        }

        super.onAdded(player);
    }

    @Override
    public void onRemoved(Player player) {
        player.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(getSpeedModifier());

        super.onRemoved(player);
    }


    private static final ResourceLocation getSpeedModName() {
        return BloodMoonRising.prefix("elusive_speed_increase");
    }

    private AttributeModifier getSpeedModifier() {
        return new AttributeModifier(getSpeedModName(), 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
