package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.LesionEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import javax.annotation.Nullable;

public class LesionModel extends GeoModel<LesionEntity> {
    @Override
    public ResourceLocation getModelResource(LesionEntity animatable) {
        return animatable.getModelLocation();
    }

    @Override
    public ResourceLocation getTextureResource(LesionEntity animatable) {
        return animatable.getTexture();
    }

    @Override
    public ResourceLocation getAnimationResource(LesionEntity animatable) {
        return animatable.getAnimationLocation();
    }

    @Override
    public void setCustomAnimations(LesionEntity animatable, long instanceId, AnimationState<LesionEntity> animationState) {

        GeoBone head = getAnimationProcessor().getBone("head");
        if (head == null || animatable.isHeartless()) {
            return;
        }

        EntityModelData data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if (data != null) {
            head.setRotX(data.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(data.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }

}
