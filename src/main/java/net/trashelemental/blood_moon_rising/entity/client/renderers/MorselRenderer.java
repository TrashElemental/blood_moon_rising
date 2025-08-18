package net.trashelemental.blood_moon_rising.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.client.models.MorselModel;
import net.trashelemental.blood_moon_rising.entity.custom.MorselEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MorselRenderer extends GeoEntityRenderer<MorselEntity> {
    public MorselRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MorselModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public ResourceLocation getTextureLocation(MorselEntity animatable) {
        return BloodMoonRising.prefix("textures/entity/morsel.png");
    }

    @Override
    public void render(MorselEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {


        poseStack.scale(0.55f, 0.55f, 0.55f);


        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}