package net.trashelemental.blood_moon_rising.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.client.models.LesionModel;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.LesionEntity;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class LesionRenderer extends GeoEntityRenderer<LesionEntity> {
    public LesionRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LesionModel());
        this.addRenderLayer(new CustomGlowmaskLayer(this));
        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(LesionEntity animatable) {
        return animatable.getTexture();
    }

    @Override
    public void render(LesionEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.scale(1f, 1f, 1f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private static class CustomGlowmaskLayer extends AutoGlowingGeoLayer<LesionEntity> {
        public CustomGlowmaskLayer(GeoRenderer<LesionEntity> renderer) {
            super(renderer);
        }

        @Override
        protected RenderType getRenderType(LesionEntity animatable, @Nullable MultiBufferSource bufferSource) {
            return RenderType.eyes(animatable.getGlowmaskTexture());
        }
    }
}