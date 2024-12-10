package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.LeechEntity;
import software.bernie.geckolib.model.GeoModel;

public class LeechModel extends GeoModel<LeechEntity> {
    @Override
    public ResourceLocation getModelResource(LeechEntity animatable) {
        return BloodMoonRising.prefix("geo/models/entities/leech.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LeechEntity animatable) {

        return BloodMoonRising.prefix("textures/entity/leech.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LeechEntity animatable) {
        return BloodMoonRising.prefix("animations/leech.animation.json");
    }

}
