package net.trashelemental.blood_moon_rising.entity.custom.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.trashelemental.blood_moon_rising.block.ModBlocks;
import net.trashelemental.blood_moon_rising.capabilities.AttachmentsRegistry;
import net.trashelemental.blood_moon_rising.capabilities.hearts.HeartData;
import net.trashelemental.blood_moon_rising.entity.ModEntities;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.TamableWoundMob;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.WoundMob;
import net.trashelemental.blood_moon_rising.item.ModItems;
import net.trashelemental.blood_moon_rising.junkyard_lib.visual.particle.ParticleMethods;
import net.trashelemental.blood_moon_rising.magic.effects.ModMobEffects;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public class BileProjectileEntity extends AbstractArrow implements GeoEntity {

    public BileProjectileEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public BileProjectileEntity(LivingEntity shooter, Level level) {
        super(ModEntities.BILE_PROJECTILE_ENTITY.get(),
                shooter, level, new ItemStack(Items.EGG), new ItemStack(Items.EGG));
    }

    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.LAVA_EXTINGUISH;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.EGG);
    }

    private float corrosionChance = 0.75f;

    public void setCorrosionChance(float chance) {
        this.corrosionChance = chance;
    }

    protected ParticleOptions getFollowingParticle() {
        return new DustParticleOptions(new Vector3f(0.702f, 0.823f, 0.471f), 1.1f);
    }

    public static boolean hasTaintedHeart(Player player) {
        HeartData data = player.getData(AttachmentsRegistry.HEART_DATA);
        return data.hasHeart(ModItems.TAINTED_HEART.get());
    }

    public static boolean shouldHurt(Entity entity) {

        if (entity instanceof Player player) {
            return (!hasTaintedHeart(player));
        }

        return !(entity instanceof WoundMob || entity instanceof TamableWoundMob);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            ParticleOptions particleoptions = this.getFollowingParticle();
            if (particleoptions != null) {
                double d0 = this.getX();
                double d1 = this.getY();
                double d2 = this.getZ();

                double offsetX = (this.level().random.nextDouble() - 0.5) * 0.3;
                double offsetY = (this.level().random.nextDouble() + 0.5) * 0.3;
                double offsetZ = (this.level().random.nextDouble() - 0.5) * 0.3;

                this.level().addParticle(particleoptions, d0 + offsetX, d1 + offsetY, d2 + offsetZ, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();

        if (!shouldHurt(entity)) {
            this.discard();
            return;
        }

        super.onHitEntity(result);

        if (!this.level().isClientSide && entity instanceof LivingEntity target && shouldHurt(entity)) {
            if (this.random.nextFloat() < corrosionChance && (!target.hasEffect(ModMobEffects.CORROSION))) {
                target.addEffect(new MobEffectInstance(ModMobEffects.CORROSION, 200));
                if (entity instanceof Player player) {
                    int arrowCount = player.getArrowCount();
                    player.setArrowCount(arrowCount - 1);
                }
            }
        }

    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            ParticleMethods.ParticlesBurst
                    (this.level(), getFollowingParticle(), this.getX(), this.getY() - 0.3, this.getZ(), 10, 1.5);
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        BlockPos pos = result.getBlockPos().above();
        BlockState state = ModBlocks.BILE_PUDDLE.get().defaultBlockState();
        Level level = this.level();

        if (level.isEmptyBlock(pos) && state.canSurvive(level, pos) && this.random.nextFloat() < 0.8f) {
            level.setBlock(pos, state, 3);
        }
    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}