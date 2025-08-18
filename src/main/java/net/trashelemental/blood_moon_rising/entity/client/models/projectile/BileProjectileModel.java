package net.trashelemental.blood_moon_rising.entity.client.models.projectile;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.BileProjectileEntity;
import software.bernie.geckolib.model.GeoModel;

public class BileProjectileModel extends GeoModel<BileProjectileEntity> {
    @Override
    public ResourceLocation getModelResource(BileProjectileEntity animatable) {
        return new ResourceLocation("blood_moon_rising","geo/models/projectiles/bile_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BileProjectileEntity animatable) {

        return new ResourceLocation("blood_moon_rising","textures/projectile/bile_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BileProjectileEntity animatable) {
        return new ResourceLocation("blood_moon_rising","animations/bile_projectile.animation.json");
    }

}
