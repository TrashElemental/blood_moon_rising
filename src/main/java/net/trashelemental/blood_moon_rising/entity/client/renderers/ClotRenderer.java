package net.trashelemental.blood_moon_rising.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.client.models.ClotModel;
import net.trashelemental.blood_moon_rising.entity.client.models.LeechModel;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.ClotEntity;
import net.trashelemental.blood_moon_rising.entity.custom.parasites.LeechEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ClotRenderer extends GeoEntityRenderer<ClotEntity> {
    public ClotRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ClotModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(ClotEntity animatable) {
        return animatable.getTexture();
    }

    @Override
    public void render(ClotEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {


        poseStack.scale(1.5f, 1.5f, 1.5f);


        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}