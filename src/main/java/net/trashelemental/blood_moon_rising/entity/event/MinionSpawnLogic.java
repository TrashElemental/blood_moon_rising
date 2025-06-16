package net.trashelemental.blood_moon_rising.entity.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.entity.ModEntities;
import net.trashelemental.blood_moon_rising.entity.custom.MorselEntity;
import net.trashelemental.blood_moon_rising.junkyard_lib.entity.method.SummonMethods;

public class MinionSpawnLogic {

    public static void spawnMorsel(ServerLevel level, Player player, int lifespan, boolean playSound) {
        MorselEntity morsel = new MorselEntity(ModEntities.MORSEL.get(), level);
        SummonMethods.summonMinion(level, player.blockPosition().below(), morsel, lifespan, false, player);
        if (playSound) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.HUSK_CONVERTED_TO_ZOMBIE, player.getSoundSource(), 0.3F, 1.3F);
        }
    }

    public static void spawnParasite(Level level, Player player, int lifespan, boolean playSound) {
        if (level instanceof ServerLevel serverLevel) {

            boolean isInWater = player.isInWater();
            var entityType = isInWater ? ModEntities.LEECH : ModEntities.MOSQUITO;
            var entity = entityType.get().create(serverLevel);

            if (entity != null) {
                SummonMethods.summonMinion(level, player.blockPosition().below(), entity, lifespan, false, player);
            }

            if (playSound) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.FROGSPAWN_HATCH, player.getSoundSource(), 1F, 0.8F);
            }
        }
    }

}
