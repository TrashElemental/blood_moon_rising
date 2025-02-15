package net.trashelemental.blood_moon_rising.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.client.models.MosquitoModel;
import net.trashelemental.blood_moon_rising.entity.custom.MosquitoEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MosquitoRenderer extends GeoEntityRenderer<MosquitoEntity> {
    public MosquitoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MosquitoModel());
    }

    @Override
    public ResourceLocation getTextureLocation(MosquitoEntity animatable) {

        if (animatable.shouldUseFullSkin()) {
            return new ResourceLocation("blood_moon_rising","textures/entity/mosquito_full.png");
        }

        return new ResourceLocation("blood_moon_rising","textures/entity/mosquito.png");
    }

    @Override
    public void render(MosquitoEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {


        poseStack.scale(0.7f, 0.7f, 0.7f);


        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
