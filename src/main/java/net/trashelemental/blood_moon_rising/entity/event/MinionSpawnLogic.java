package net.trashelemental.blood_moon_rising.entity.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.entity.ModEntities;
import net.trashelemental.blood_moon_rising.entity.custom.MorselEntity;

public class MinionSpawnLogic {

    public static void spawnMorsel(ServerLevel level, Player player, int age) {
        MorselEntity morsel = new MorselEntity(ModEntities.MORSEL.get(), level);
        morsel.moveTo(player.getX(), player.getY(), player.getZ(), 0, 0);
        morsel.setTame(true, false);
        morsel.setOwnerUUID(player.getUUID());
        morsel.setAge(age);
        level.addFreshEntity(morsel);
    }

    public static void spawnParasite(Level level, LivingEntity player, int age) {
        if (level instanceof ServerLevel serverLevel) {

            boolean isInWater = player.isInWater();
            var entityType = isInWater ? ModEntities.LEECH : ModEntities.MOSQUITO;
            var entity = entityType.get().create(serverLevel);

            if (entity != null) {
                entity.moveTo(player.getX(), player.getY(), player.getZ(), level.getRandom().nextFloat() * 360F, 0);
                entity.setTame(true, false);
                entity.setOwnerUUID(player.getUUID());
                entity.setAge(age);
                serverLevel.addFreshEntity(entity);

                serverLevel.sendParticles(ParticleTypes.DAMAGE_INDICATOR, player.getX(), player.getY(), player.getZ(),
                        3, 0.5, 0.5, 0.5, 0.1);
            }
        }
    }

}
