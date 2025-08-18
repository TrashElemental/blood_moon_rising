package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.entity.event.MinionSpawnLogic;

import java.util.UUID;

/**
 * Causes the attacker to take damage whenever the player is damaged.
 */
public class DividingHeartEffect extends AbstractHeartEffect {

    private static final UUID UUID = java.util.UUID.fromString("9a4c2b1c-bf84-4f20-bf18-e063b14c12ed");

    public DividingHeartEffect() {
        super(UUID, "Dividing", -2.0);
    }

    @Override
    public void onHurt(Player player, LivingEntity attacker, DamageSource source, float amount) {
        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) return;

        MinionSpawnLogic.spawnMorsel(serverLevel, player, 300, true);
    }
}
