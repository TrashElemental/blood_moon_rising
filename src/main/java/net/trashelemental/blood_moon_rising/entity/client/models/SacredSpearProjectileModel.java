package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.SacredSpearProjectileEntity;
import software.bernie.geckolib.model.GeoModel;

public class SacredSpearProjectileModel extends GeoModel<SacredSpearProjectileEntity> {
    @Override
    public ResourceLocation getModelResource(SacredSpearProjectileEntity animatable) {
        return new ResourceLocation("blood_moon_rising","geo/models/projectiles/sacred_spear_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SacredSpearProjectileEntity animatable) {

        return new ResourceLocation("blood_moon_rising","textures/projectile/sacred_spear_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SacredSpearProjectileEntity animatable) {
        return new ResourceLocation("blood_moon_rising","animations/sacred_spear_projectile.animation.json");
    }

}
