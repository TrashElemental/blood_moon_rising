package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.custom.LeechEntity;
import software.bernie.geckolib.model.GeoModel;

public class LeechModel extends GeoModel<LeechEntity> {
    @Override
    public ResourceLocation getModelResource(LeechEntity animatable) {
        return new ResourceLocation("blood_moon_rising","geo/models/entities/leech.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LeechEntity animatable) {

        return new ResourceLocation("blood_moon_rising","textures/entity/leech.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LeechEntity animatable) {
        return new ResourceLocation("blood_moon_rising","animations/leech.animation.json");
    }

}
