package net.trashelemental.blood_moon_rising.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.entity.client.models.OrganelleModel;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.OrganelleEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class OrganelleRenderer extends GeoEntityRenderer<OrganelleEntity> {
    public OrganelleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new OrganelleModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(OrganelleEntity animatable) {
        return animatable.getTexture();
    }

    @Override
    public void render(OrganelleEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        poseStack.scale(1f, 1f, 1f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
