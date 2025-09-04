package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.LimbEntity;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import javax.annotation.Nullable;

public class LimbModel extends GeoModel<LimbEntity> {
    @Override
    public ResourceLocation getModelResource(LimbEntity animatable) {
        return animatable.getModelLocation();
    }

    @Override
    public ResourceLocation getTextureResource(LimbEntity animatable) {
        return animatable.getTexture();
    }

    @Override
    public ResourceLocation getAnimationResource(LimbEntity animatable) {
        return animatable.getAnimationLocation();
    }

    @Override
    public void setCustomAnimations(LimbEntity entity, long uniqueID, @Nullable AnimationState<LimbEntity> state) {
        super.setCustomAnimations(entity, uniqueID, state);
        GeoBone head = getAnimationProcessor().getBone("head");

        if (!(state == null) && !state.isCurrentAnimationStage("WAVE")) {
            if (head != null) {
                EntityModelData entityData = state.getData(DataTickets.ENTITY_MODEL_DATA);

                head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
                head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
            }
        }

    }

}
