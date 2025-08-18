package net.trashelemental.blood_moon_rising.entity.custom.projectiles;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.trashelemental.blood_moon_rising.entity.ModEntities;
import net.trashelemental.blood_moon_rising.magic.effects.event.HemorrhageLogic;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class SacredSpearProjectileEntity extends AbstractArrow implements GeoEntity {

    public SacredSpearProjectileEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public SacredSpearProjectileEntity(LivingEntity shooter, Level level) {
        super(ModEntities.SACRED_SPEAR_PROJECTILE_ENTITY.get(),
                shooter, level);
    }

    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.EGG);
    }

    private float hemorrhageChance = 0.5f;

    public void setHemorrhageChance(float chance) {
        this.hemorrhageChance = chance;
    }

    protected ParticleOptions getFollowingParticle() {
        return new DustParticleOptions(new Vector3f(0.6f, 0.02f, 0.02f), 1.1f);
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

                this.level().addParticle(particleoptions, d0, d1 + 0.25, d2, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();


        if (!this.level().isClientSide && entity instanceof LivingEntity target) {
            if (this.random.nextFloat() < hemorrhageChance) {
                HemorrhageLogic.applyHemorrhage(target, this.getOwner(), 240);
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
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
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