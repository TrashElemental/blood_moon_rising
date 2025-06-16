package net.trashelemental.blood_moon_rising.entity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.trashelemental.blood_moon_rising.entity.ModEntities;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.WoundMob;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.MosquitoEntity;

public class WoundMobSummonMethods {

    public static void summonWoundMob(ServerLevel level, BlockPos pos, WoundMob entity, float clotChance, float parasiteChance, boolean isSpecialSummon) {
        if (entity != null) {
            entity.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0);
            level.addFreshEntity(entity);
            entity.setClotChance(clotChance);
            entity.setParasiteChance(parasiteChance);
            entity.setIsSpecialSummon(isSpecialSummon);
        }
    }

    public static void summonParasites(ServerLevel level, LivingEntity entity, int lifespan, boolean persistent) {
        BlockPos entityPos = entity.getOnPos();
        boolean isInWater = entity.isInWater();
        var entityType = isInWater ? ModEntities.LEECH : ModEntities.MOSQUITO;

        int count = 2 + level.getRandom().nextInt(2);

        for (int i = 0; i < count; i++) {
            var parasite = entityType.get().create(level);
            if (parasite != null) {

                int finalLifespan = lifespan;

                if (entityType == ModEntities.MOSQUITO && level.getRandom().nextFloat() < 0.01f) {
                    if (parasite instanceof MosquitoEntity mosquito) {
                        mosquito.setRareSpawn(true);
                        finalLifespan = 300;
                    }
                }

                parasite.setLifespan(finalLifespan, persistent);

                double x = entityPos.getX() + 0.5 + (level.getRandom().nextDouble() - 0.5);
                double y = entityPos.getY() + 0.5;
                double z = entityPos.getZ() + 0.5 + (level.getRandom().nextDouble() - 0.5);
                parasite.moveTo(x, y, z);

                level.addFreshEntity(parasite);
            }
        }
        level.playSound(entity, entityPos, SoundEvents.FROGSPAWN_HATCH, entity.getSoundSource(), 1f, 0.8f);
    }


}
