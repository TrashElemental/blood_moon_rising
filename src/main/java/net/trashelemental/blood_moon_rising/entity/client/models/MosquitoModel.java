package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.MosquitoEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class MosquitoModel extends GeoModel<MosquitoEntity> {
    @Override
    public ResourceLocation getModelResource(MosquitoEntity animatable) {
        return BloodMoonRising.prefix("geo/models/entities/mosquito.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MosquitoEntity animatable) {

        if (animatable.shouldUseFullSkin()) {
            return BloodMoonRising.prefix("textures/entity/mosquito_full.png");
        }

        return BloodMoonRising.prefix("textures/entity/mosquito.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MosquitoEntity animatable) {
        return BloodMoonRising.prefix("animations/mosquito.animation.json");
    }

    @Override
    public void setCustomAnimations(MosquitoEntity animatable, long instanceId, AnimationState<MosquitoEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
