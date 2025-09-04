package net.trashelemental.blood_moon_rising.entity.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.ModEntities;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.ClotEntity;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.MouthEntity;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.OrganelleEntity;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.LeechEntity;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.MosquitoEntity;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.ParasiteEntity;
import net.trashelemental.blood_moon_rising.junkyard_lib.entity.method.SummonMethods;

public class MouthEvents {

    public static void spawnReinforcements(MouthEntity mouth) {

        Level level = mouth.level();
        Vec3 look = mouth.getLookAngle();
        Vec3 spawnVec = mouth.position().add(look.x, 0, look.z);
        BlockPos spawnPos = new BlockPos(
                (int) Math.floor(spawnVec.x),
                (int) Math.floor(spawnVec.y),
                (int) Math.floor(spawnVec.z)
        );

        spawningVFX(mouth);

        if (level instanceof ServerLevel serverLevel) {
            BloodMoonRising.queueServerWork(65, () -> {
                spawnEntities(mouth, serverLevel, spawnPos);
            });
        }
    }

    public static void spawnEntities(MouthEntity mouth, ServerLevel level, BlockPos spawnPos) {
        int choice = level.random.nextInt(3);

        switch (choice) {
            case 0 -> {
                int organelleAmount = 2 + level.random.nextInt(2);
                for (int i = 0; i < organelleAmount; i++) {
                    OrganelleEntity organelle = new OrganelleEntity(ModEntities.ORGANELLE.get(), level);
                    WoundMobSummonMethods.summonWoundMob(level, spawnPos, organelle, 0, 0, false);
                }
            }
            case 1 -> {
                int parasiteAmount = 3 + level.random.nextInt(2);
                for (int i = 0; i < parasiteAmount; i++) {
                    ParasiteEntity parasite = level.random.nextBoolean() ?
                            new MosquitoEntity(ModEntities.MOSQUITO.get(), level) :
                            new LeechEntity(ModEntities.LEECH.get(), level);
                    SummonMethods.summonEntity(level, spawnPos, parasite);
                    parasite.setLifespan(300, false);
                }
            }
            case 2 -> {
                ClotEntity clot = new ClotEntity(ModEntities.CLOT.get(), level);
                WoundMobSummonMethods.summonWoundMob(level, spawnPos, clot, 0, 0, false);
            }
        }
    }

    public static void spawningVFX(MouthEntity mouth) {
        BloodMoonRising.queueServerWork(7, () -> {
            mouth.playSound(SoundEvents.MULE_EAT, 1f, 1f);
        });
        BloodMoonRising.queueServerWork(12, () -> {
            mouth.playSound(SoundEvents.MULE_EAT, 1f, 1.1f);
        });
        BloodMoonRising.queueServerWork(20, () -> {
            mouth.playSound(SoundEvents.MULE_EAT, 1f, 0.9f);
        });
        BloodMoonRising.queueServerWork(30, () -> {
            mouth.playSound(SoundEvents.MULE_EAT, 1f, 1f);
        });
        BloodMoonRising.queueServerWork(60, () -> {
            mouth.playSound(SoundEvents.LLAMA_SPIT, 1f, 0.4f);
        });
    }
}
