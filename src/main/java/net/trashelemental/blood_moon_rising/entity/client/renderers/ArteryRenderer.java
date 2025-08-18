package net.trashelemental.blood_moon_rising.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.client.models.ArteryModel;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.ArteryEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ArteryRenderer extends GeoEntityRenderer<ArteryEntity> {
    public ArteryRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ArteryModel());
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(ArteryEntity animatable) {
        return animatable.getTexture();
    }

    @Override
    public void render(ArteryEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        poseStack.scale(1.5f, 1.5f, 1.5f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
