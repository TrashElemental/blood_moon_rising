package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.MolarEntity;
import software.bernie.geckolib.model.GeoModel;

public class MolarModel extends GeoModel<MolarEntity> {
    @Override
    public ResourceLocation getModelResource(MolarEntity animatable) {
        return animatable.getModelLocation();
    }

    @Override
    public ResourceLocation getTextureResource(MolarEntity animatable) {
        return animatable.getTexture();
    }

    @Override
    public ResourceLocation getAnimationResource(MolarEntity animatable) {
        return animatable.getAnimationLocation();
    }

}
