package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.OrganelleEntity;
import software.bernie.geckolib.model.GeoModel;

public class OrganelleModel extends GeoModel<OrganelleEntity> {
    @Override
    public ResourceLocation getModelResource(OrganelleEntity animatable) {
        return animatable.getModelLocation();
    }

    @Override
    public ResourceLocation getTextureResource(OrganelleEntity animatable) {
        return animatable.getTexture();
    }

    @Override
    public ResourceLocation getAnimationResource(OrganelleEntity animatable) {
        return animatable.getAnimationLocation();
    }

}
