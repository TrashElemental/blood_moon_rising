package net.trashelemental.blood_moon_rising.junkyard_lib.entity.method;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.trashelemental.blood_moon_rising.junkyard_lib.entity.MinionEntity;

public class SummonMethods {

    /**
     * Summons a basic entity.
     */
    public static void summonEntity(Level level, BlockPos summonPos, LivingEntity entityToSummon) {
        if (!level.isClientSide) {
            if (entityToSummon != null) {
                entityToSummon.moveTo(summonPos.getX() + 0.5, summonPos.getY() + 1, summonPos.getZ() + 0.5, 0, 0);
                level.addFreshEntity(entityToSummon);
            }
        }
    }

    /**
     * Summons a tamable entity and tames it by the player passed in.
     */
    public static void summonTamedAnimal(Level level, BlockPos pos, TamableAnimal entityToSummon, Player player) {
        if (!level.isClientSide) {
            if (entityToSummon != null) {
                entityToSummon.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0);
                entityToSummon.setTame(true);
                entityToSummon.setOwnerUUID(player.getUUID());
                level.addFreshEntity(entityToSummon);
            }
        }
    }

    /**
     * Summons a tamed minion (see MinionEntity) and sets its lifespan.
     * Set persistent to true for it to not despawn via lifespan.
     */
    public static void summonMinion(Level level, BlockPos pos, MinionEntity entityToSummon, int lifespan, boolean persistent, Player player) {
        if (!level.isClientSide) {
            if (entityToSummon != null) {
                entityToSummon.setLifespan(lifespan, persistent);
                entityToSummon.moveTo(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0);
                entityToSummon.setTame(true);
                entityToSummon.setOwnerUUID(player.getUUID());
                level.addFreshEntity(entityToSummon);
            }
        }
    }




}
