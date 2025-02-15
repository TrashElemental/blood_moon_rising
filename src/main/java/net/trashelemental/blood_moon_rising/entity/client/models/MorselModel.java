package net.trashelemental.blood_moon_rising.entity.client.models;

import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.custom.MorselEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import javax.annotation.Nullable;

public class MorselModel extends GeoModel<MorselEntity> {
    @Override
    public ResourceLocation getModelResource(MorselEntity animatable) {
        return new ResourceLocation("blood_moon_rising","geo/models/entities/morsel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MorselEntity animatable) {

        return new ResourceLocation("blood_moon_rising","textures/entity/morsel.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MorselEntity animatable) {
        return new ResourceLocation("blood_moon_rising","animations/morsel.animation.json");
    }


    @Override
    public void setCustomAnimations(MorselEntity entity, long uniqueID, @Nullable AnimationState<MorselEntity> customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraData().get(DataTickets.ENTITY_MODEL_DATA);
        head.setRotX(extraData.headPitch() * 0.017453292F);
        head.setRotY(extraData.netHeadYaw() * 0.017453292F);
    }
}
