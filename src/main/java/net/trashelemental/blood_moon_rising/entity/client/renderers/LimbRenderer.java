package net.trashelemental.blood_moon_rising.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.trashelemental.blood_moon_rising.entity.client.models.LimbModel;
import net.trashelemental.blood_moon_rising.entity.custom.blood_moon.LimbEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LimbRenderer extends GeoEntityRenderer<LimbEntity> {
    public LimbRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LimbModel());

        this.addRenderLayer(new LimbHeldItemLayer<>(
                this,
                (bone, animatable) -> {
                    if ("mouth_anchor".equals(bone.getName()) && animatable.getCarriedItem() != null) {
                        return animatable.getCarriedItem();
                    }
                    return ItemStack.EMPTY;
                },
                (bone, animatable) -> null,
                0.6f, 0.6f, 0.6f,
                90f, 0f, 0f
        ));

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(LimbEntity animatable) {
        return animatable.getTexture();
    }

    @Override
    public void render(LimbEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        poseStack.scale(1f, 1f, 1f);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
