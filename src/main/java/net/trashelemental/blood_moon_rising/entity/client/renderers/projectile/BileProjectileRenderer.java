package net.trashelemental.blood_moon_rising.entity.client.renderers.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.trashelemental.blood_moon_rising.entity.client.models.projectile.BileProjectileModel;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.BileProjectileEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BileProjectileRenderer extends GeoEntityRenderer<BileProjectileEntity> {
    public BileProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BileProjectileModel());
    }

    @Override
    public ResourceLocation getTextureLocation(BileProjectileEntity animatable) {
        return new ResourceLocation("blood_moon_rising","textures/projectile/bile_projectile.png");
    }

    @Override
    public void render(BileProjectileEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
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
