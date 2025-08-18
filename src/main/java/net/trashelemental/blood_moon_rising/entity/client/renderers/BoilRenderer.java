package net.trashelemental.blood_moon_rising.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.client.models.BoilModel;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.BoilEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BoilRenderer extends GeoEntityRenderer<BoilEntity> {
    public BoilRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BoilModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(BoilEntity animatable) {
        return animatable.getTexture();
    }

    @Override
    public void render(BoilEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        poseStack.scale(1f, 1f, 1f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
