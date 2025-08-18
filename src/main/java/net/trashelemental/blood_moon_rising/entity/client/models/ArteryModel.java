package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.ArteryEntity;
import software.bernie.geckolib.model.GeoModel;

public class ArteryModel extends GeoModel<ArteryEntity> {
    @Override
    public ResourceLocation getModelResource(ArteryEntity animatable) {
        return animatable.getModelLocation();
    }

    @Override
    public ResourceLocation getTextureResource(ArteryEntity animatable) {
        return animatable.getTexture();
    }

    @Override
    public ResourceLocation getAnimationResource(ArteryEntity animatable) {
        return animatable.getAnimationLocation();
    }

}
