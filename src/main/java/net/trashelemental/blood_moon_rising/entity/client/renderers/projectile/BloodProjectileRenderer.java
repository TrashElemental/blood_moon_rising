package net.trashelemental.blood_moon_rising.entity.client.renderers.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.trashelemental.blood_moon_rising.entity.client.models.projectile.BloodProjectileModel;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.BloodProjectileEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BloodProjectileRenderer extends GeoEntityRenderer<BloodProjectileEntity> {
    public BloodProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BloodProjectileModel());
    }

    @Override
    public ResourceLocation getTextureLocation(BloodProjectileEntity animatable) {
        return new ResourceLocation("blood_moon_rising","textures/projectile/blood_projectile.png");
    }

    @Override
    public void render(BloodProjectileEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        float lerpedYaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float lerpedPitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());

        poseStack.mulPose(Axis.YP.rotationDegrees(lerpedYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-lerpedPitch));

        poseStack.scale(0.5f, 0.5f, 0.5f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }
}
