package net.trashelemental.blood_moon_rising.entity.custom.projectiles;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.trashelemental.blood_moon_rising.entity.ModEntities;
import net.trashelemental.blood_moon_rising.item.ModItems;
import net.trashelemental.blood_moon_rising.magic.effects.events.HemorrhageLogic;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public class SacredSpearProjectileEntity extends AbstractArrow implements GeoEntity {

    public SacredSpearProjectileEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public SacredSpearProjectileEntity(LivingEntity shooter, Level level) {
        super(ModEntities.SACRED_SPEAR_PROJECTILE_ENTITY.get(),
                shooter, level, new ItemStack(Items.EGG), new ItemStack(ModItems.SACRED_SPEAR.get()));
    }

    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.EGG);
    }

    private float hemorrhageChance = 0.5f;

    public void setHemorrhageChance(float chance) {
        this.hemorrhageChance = chance;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();

        if (!this.level().isClientSide && entity instanceof LivingEntity target) {
            if (this.random.nextFloat() < hemorrhageChance) {
                HemorrhageLogic.applyHemorrhage(target, this.getOwner(), 240);
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
