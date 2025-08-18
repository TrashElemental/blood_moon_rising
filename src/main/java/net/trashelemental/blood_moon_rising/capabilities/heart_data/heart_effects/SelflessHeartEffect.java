package net.trashelemental.blood_moon_rising.capabilities.heart_data.heart_effects;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.trashelemental.blood_moon_rising.junkyard_lib.util.UtilMethods;
import net.trashelemental.blood_moon_rising.junkyard_lib.visual.particle.ParticleMethods;

import java.util.List;
import java.util.UUID;

/**
 * When the player interacts with other entities that are missing health, heal them at the cost of some self-damage.
 * Nearby tamed mobs receive regeneration.
 */
public class SelflessHeartEffect extends AbstractHeartEffect {

    private static final UUID UUID = java.util.UUID.fromString("9a4c2b1c-bf84-4f10-bf18-e063b14a12ec");

    public SelflessHeartEffect() {
        super(UUID, "Selfless", -2.0);
    }

    @Override
    public void onInteract(Player player, LivingEntity entity) {

        if (entity.getHealth() < entity.getMaxHealth() && player.getHealth() > 2) {
            UtilMethods.damageEntity(player, DamageTypes.MAGIC, 2);
            entity.heal(5);
            ParticleMethods.ParticlesAroundServerSide(player.level(), ParticleTypes.HEART,
                    entity.getX(), entity.getY() + 1, entity.getZ(), 5, 2);
        }

        super.onInteract(player, entity);
    }

    @Override
    public void onTick(Player player) {
        if (player.tickCount % 10 != 0) return;
        final double radius = 4.0;

        List<LivingEntity> nearbyAllies = player.level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(radius), entity -> {
                    if (entity instanceof TamableAnimal tamable &&
                            tamable.isTame() &&
                            player.getUUID().equals(tamable.getOwnerUUID())) {
                        return true;
                    }
                    return entity.isAlliedTo(player);
                }
        );

        for (LivingEntity mob : nearbyAllies) {
            mob.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0, true, true));
        }

        super.onTick(player);
    }
}
