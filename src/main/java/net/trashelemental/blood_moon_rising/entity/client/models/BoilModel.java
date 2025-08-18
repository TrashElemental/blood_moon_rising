package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.BoilEntity;
import software.bernie.geckolib.model.GeoModel;

public class BoilModel extends GeoModel<BoilEntity> {
    @Override
    public ResourceLocation getModelResource(BoilEntity animatable) {
        return animatable.getModelLocation();
    }

    @Override
    public ResourceLocation getTextureResource(BoilEntity animatable) {
        return animatable.getTexture();
    }

    @Override
    public ResourceLocation getAnimationResource(BoilEntity animatable) {
        return animatable.getAnimationLocation();
    }

}
