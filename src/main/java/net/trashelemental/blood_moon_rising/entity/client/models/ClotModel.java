package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.ClotEntity;
import software.bernie.geckolib.model.GeoModel;

public class ClotModel extends GeoModel<ClotEntity> {
    @Override
    public ResourceLocation getModelResource(ClotEntity animatable) {
        return animatable.getModelLocation();
    }

    @Override
    public ResourceLocation getTextureResource(ClotEntity animatable) {
        return animatable.getTexture();
    }

    @Override
    public ResourceLocation getAnimationResource(ClotEntity animatable) {
        return animatable.getAnimationLocation();
    }

}
