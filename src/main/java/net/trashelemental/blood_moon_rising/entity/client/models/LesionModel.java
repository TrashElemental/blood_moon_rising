package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.LesionEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
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
    public void setCustomAnimations(LesionEntity entity, long uniqueID, @Nullable AnimationState<LesionEntity> customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        if (!entity.isHeartless()) {
            EntityModelData data = (EntityModelData) customPredicate.getExtraData().get(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(data.headPitch() * 0.017453292F);
            head.setRotY(data.netHeadYaw() * 0.017453292F);
        }
    }

}
