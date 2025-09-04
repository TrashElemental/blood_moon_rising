package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.LimbEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
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
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) state.getExtraData().get(DataTickets.ENTITY_MODEL_DATA);


        if (!state.isCurrentAnimationStage("WAVE")) {
            head.setRotX(extraData.headPitch() * 0.017453292F);
            head.setRotY(extraData.netHeadYaw() * 0.017453292F);
        }

    }

}
