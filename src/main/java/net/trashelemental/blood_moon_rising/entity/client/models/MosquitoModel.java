package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.custom.MosquitoEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import javax.annotation.Nullable;

public class MosquitoModel extends GeoModel<MosquitoEntity> {
    @Override
    public ResourceLocation getModelResource(MosquitoEntity animatable) {
        return new ResourceLocation("blood_moon_rising","geo/models/entities/mosquito.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MosquitoEntity animatable) {

        if (animatable.shouldUseFullSkin()) {
            return new ResourceLocation("blood_moon_rising","textures/entity/mosquito_full.png");
        }

        return new ResourceLocation("blood_moon_rising","textures/entity/mosquito.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MosquitoEntity animatable) {
        return new ResourceLocation("blood_moon_rising","animations/mosquito.animation.json");
    }

    @Override
    public void setCustomAnimations(MosquitoEntity entity, long uniqueID, @Nullable AnimationState<MosquitoEntity> customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraData().get(DataTickets.ENTITY_MODEL_DATA);
        head.setRotX(extraData.headPitch() * 0.017453292F);
        head.setRotY(extraData.netHeadYaw() * 0.017453292F);
    }

}
