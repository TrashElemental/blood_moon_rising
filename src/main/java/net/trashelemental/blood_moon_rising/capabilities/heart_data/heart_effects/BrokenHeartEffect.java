package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.IHeartEffect;
import net.trashelemental.blood_moon_rising.junkyard_lib.util.UtilMethods;
import net.trashelemental.blood_moon_rising.util.event.AdjustHealthEvent;

import java.util.UUID;

public class BrokenHeartEffect extends AbstractHeartEffect {

    public BrokenHeartEffect() {
        super (HEALTH_REDUCTION_UUID, "Broken", -6.0);
    }

    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("9a4c2b1c-bf84-4f20-bf18-e063b14a12ea");
    private static final UUID HEALTH_REDUCTION_UUID = UUID.fromString("9a4c2b1c-bf84-4f20-bf18-e063b14a12eb");

    @Override
    public void onAdded(Player player) {
        UtilMethods.applyModifier(player, Attributes.ATTACK_DAMAGE, getDamageModifier());

       super.onAdded(player);
    }

    @Override
    public void onRemoved(Player player) {
        UtilMethods.removeModifier(player, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_UUID);

        super.onRemoved(player);
    }

    private AttributeModifier getDamageModifier() {
        return new AttributeModifier(ATTACK_DAMAGE_UUID, "Damage Bonus", 6.0, AttributeModifier.Operation.ADDITION);
    }
}
