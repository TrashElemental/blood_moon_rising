package net.trashelemental.blood_moon_rising.entity.custom.blood_moon;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.blood_moon.BloodMoonManager;
import net.trashelemental.blood_moon_rising.entity.event.WoundMobSummonMethods;
import net.trashelemental.blood_moon_rising.junkyard_lib.entity.TamableEntity;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;
import net.trashelemental.blood_moon_rising.magic.effects.event.HemorrhageLogic;

import javax.annotation.Nullable;

public class TamableWoundMob extends TamableEntity {
    protected float CLOT_SPAWN_CHANCE;
    protected float PARASITE_SPAWN_CHANCE;
    protected boolean IS_SPECIAL_SUMMON = false;
    private int decayCountdown = 100;

    public TamableWoundMob(EntityType<? extends TamableEntity> type, Level level, Item tameItem, Item breedItem, float clotSpawnChance, float parasiteSpawnChance) {
        super(type, level, tameItem, breedItem);
        this.CLOT_SPAWN_CHANCE = clotSpawnChance;
        this.PARASITE_SPAWN_CHANCE = parasiteSpawnChance;
    }

    public TamableWoundMob(EntityType<? extends TamableEntity> type, Level level, Item tameItem, Item breedItem) {
        this(type, level, tameItem, breedItem, 0.0f, 0.0f);
    }

    public void setClotChance(float chance) {
        this.CLOT_SPAWN_CHANCE = chance;
    }
    public void setParasiteChance(float chance) {
        this.PARASITE_SPAWN_CHANCE = chance;
    }
    public void setIsSpecialSummon(boolean special) {
        this.IS_SPECIAL_SUMMON = special;
    }
    public boolean isSpecialSummon() {
        return IS_SPECIAL_SUMMON;
    }

    public boolean isBloodMoonActive(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return BloodMoonManager.isBloodMoon(serverLevel);
        }
        return false;
    }
    public boolean isBloodMoon() {
        return isBloodMoonActive(this.level());
    }


    public ResourceLocation getTexture() {
        return null;
    }

    public ResourceLocation getModelLocation() {
        return null;
    }

    public ResourceLocation getAnimationLocation() {
        return null;
    }


    /**
     * When summoned via spawn egg or command, they will be set to not decay.
     */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        if (spawnType == MobSpawnType.SPAWN_EGG || spawnType == MobSpawnType.COMMAND) {
            this.setIsSpecialSummon(true);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsSpecialSummon", this.IS_SPECIAL_SUMMON);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("IsSpecialSummon")) {
            this.IS_SPECIAL_SUMMON = tag.getBoolean("IsSpecialSummon");
        }
    }

    public static boolean spawnRules(EntityType<? extends TamableWoundMob> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos) {
        BlockPos belowPos = pos.below();

        return spawnType == MobSpawnType.SPAWNER || level.getBlockState(belowPos).isValidSpawn(level, belowPos, type);
    }


    /**
     * Wound mobs are especially vulnerable to fire and smite damage.
     */
    public boolean isSensitiveToDamage(DamageSource source, @Nullable LivingEntity attacker) {
        boolean isSmite = false;
        if (attacker != null) {
            int smiteLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SMITE, attacker.getMainHandItem());
            isSmite = smiteLevel > 0;
        }
        return HemorrhageLogic.isFireTypeDamage(source) || isSmite;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        LivingEntity attacker = (source.getEntity() instanceof LivingEntity le) ? le : null;

        if (HemorrhageLogic.isFireTypeDamage(source)) {
            amount += 1.5f;
        }

        if (attacker != null) {
            int smiteLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SMITE, attacker.getMainHandItem());
            if (smiteLevel > 0) {
                amount += smiteLevel * 2.5f;
            }
        }
        return super.hurt(source, amount);
    }

    /**
     * When a blood moon is not happening, Wound mobs will rapidly die off.
     * Ignored if they were specially summoned or have the amnion effect.
     */
    public boolean shouldDecay() {
        return !isSpecialSummon() && !this.hasEffect(ModMobEffects.KINSHIP.get());
    }

    @Override
    public void tick() {
        super.tick();

        if (!isBloodMoon() && shouldDecay()) {
            if (decayCountdown > 0) {
                decayCountdown--;
            } else {
                decayCountdown = 100;
                this.hurt(this.level().damageSources().generic(), this.getMaxHealth() / 5f);
            }
        }
    }

    /**
     * When a wound mob dies, it will check to see if it should spawn reinforcements, in the form
     * of clots and/or parasites.
     */
    @Override
    public void die(DamageSource source) {
        super.die(source);

        if (source.getEntity() instanceof LivingEntity attacker) {
            if ((this.level() instanceof ServerLevel level) && !isSensitiveToDamage(source, attacker)) {
                RandomSource random = this.getRandom();

                if (random.nextFloat() < CLOT_SPAWN_CHANCE) {
                    BloodMoonRising.queueServerWork(20, () -> WoundMobSummonMethods.summonClot(level, this));
                }

                if (random.nextFloat() < PARASITE_SPAWN_CHANCE) {
                    WoundMobSummonMethods.summonParasites(level, this, 600, false);
                }
            }
        }
    }

    static boolean isWoundAttackTarget(LivingEntity entity) {
        return !invalidTarget(entity) && (entity instanceof Raider || entity.hasEffect(ModMobEffects.SCORN.get()));
    }

    static boolean invalidTarget(LivingEntity entity) {
        return entity.hasEffect(ModMobEffects.KINSHIP.get());
    }

}
