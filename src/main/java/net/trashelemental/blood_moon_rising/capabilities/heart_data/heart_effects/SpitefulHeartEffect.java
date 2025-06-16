package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.capabilities.heart_data.IHeartEffect;
import net.trashelemental.blood_moon_rising.junkyard_lib.util.UtilMethods;
import net.trashelemental.blood_moon_rising.util.event.AdjustHealthEvent;

import java.util.List;
import java.util.UUID;

/**
 * Causes the attacker to take damage whenever the player is damaged.
 */
public class SpitefulHeartEffect extends AbstractHeartEffect {

    private static final UUID UUID = java.util.UUID.fromString("9a4c2b1c-bf84-4f20-bf18-e063b14a12ed");

    public SpitefulHeartEffect() {
        super(UUID, "Spiteful", -2.0);
    }

    @Override
    public void onHurt(Player player, LivingEntity attacker, DamageSource source, float amount) {
        UtilMethods.damageEntity(attacker, DamageTypes.THORNS, 2);
    }
}
