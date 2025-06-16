package net.trashelemental.blood_moon_rising.entity.client.renderers.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.client.models.projectiles.SacredSpearProjectileModel;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.SacredSpearProjectileEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SacredSpearProjectileRenderer extends GeoEntityRenderer<SacredSpearProjectileEntity> {
    public SacredSpearProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SacredSpearProjectileModel());
    }

    @Override
    public ResourceLocation getTextureLocation(SacredSpearProjectileEntity animatable) {
        return BloodMoonRising.prefix("textures/projectile/sacred_spear_projectile.png");
    }

    @Override
    public void render(SacredSpearProjectileEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        float lerpedYaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float lerpedPitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());

        poseStack.mulPose(Axis.YP.rotationDegrees(lerpedYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-lerpedPitch));

        poseStack.scale(1f, 1f, 1f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }
}
