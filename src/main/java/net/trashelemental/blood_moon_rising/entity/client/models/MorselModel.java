package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.MorselEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class MorselModel extends GeoModel<MorselEntity> {
    @Override
    public ResourceLocation getModelResource(MorselEntity animatable) {
        return BloodMoonRising.prefix("geo/models/entities/morsel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MorselEntity animatable) {

        return BloodMoonRising.prefix("textures/entity/morsel.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MorselEntity animatable) {
        return BloodMoonRising.prefix("animations/morsel.animation.json");
    }

    @Override
    public void setCustomAnimations(MorselEntity animatable, long instanceId, AnimationState<MorselEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
