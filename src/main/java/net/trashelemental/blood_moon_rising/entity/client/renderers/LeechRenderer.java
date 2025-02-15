package net.trashelemental.blood_moon_rising.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.client.models.LeechModel;
import net.trashelemental.blood_moon_rising.entity.custom.LeechEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LeechRenderer extends GeoEntityRenderer<LeechEntity> {
    public LeechRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LeechModel());
    }

    @Override
    public ResourceLocation getTextureLocation(LeechEntity animatable) {
        return new ResourceLocation("blood_moon_rising","textures/entity/leech.png");
    }

    @Override
    public void render(LeechEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {


        poseStack.scale(1.5f, 1.5f, 1.5f);


        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
