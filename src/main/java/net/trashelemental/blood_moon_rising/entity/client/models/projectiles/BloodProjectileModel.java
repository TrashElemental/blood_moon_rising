package net.trashelemental.blood_moon_rising.entity.client.models.projectiles;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.BloodProjectileEntity;
import software.bernie.geckolib.model.GeoModel;

public class BloodProjectileModel extends GeoModel<BloodProjectileEntity> {
    @Override
    public ResourceLocation getModelResource(BloodProjectileEntity animatable) {
        return BloodMoonRising.prefix("geo/models/projectiles/bile_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BloodProjectileEntity animatable) {

        return BloodMoonRising.prefix("textures/projectile/blood_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BloodProjectileEntity animatable) {
        return BloodMoonRising.prefix("animations/bile_projectile.animation.json");
    }

}
