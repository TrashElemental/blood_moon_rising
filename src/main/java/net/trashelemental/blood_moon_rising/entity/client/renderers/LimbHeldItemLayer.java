package net.trashelemental.blood_moon_rising.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import java.util.function.BiFunction;

/** Subclass of BlockAndItemGeoLayer that lets the Limb hold items horizontally in its mouth like a fox */

public class LimbHeldItemLayer<T extends GeoAnimatable> extends BlockAndItemGeoLayer<T> {
    private final float scaleX, scaleY, scaleZ;
    private final float rotX, rotY, rotZ;

    public LimbHeldItemLayer(
            GeoRenderer<T> renderer,
            BiFunction<GeoBone, T, ItemStack> stackForBone,
            BiFunction<GeoBone, T, BlockState> blockForBone,
            float scaleX, float scaleY, float scaleZ,
            float rotX, float rotY, float rotZ) {
        super(renderer, stackForBone, blockForBone);
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    @Override
    protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, T animatable,
                                      MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
        poseStack.pushPose();

        if (stack.getItem() instanceof BlockItem) {
            poseStack.scale(0.2f, 0.2f, 0.2f);
            poseStack.mulPose(Axis.XP.rotationDegrees(90f));
        } else {
            poseStack.scale(scaleX, scaleY, scaleZ);
            poseStack.mulPose(Axis.XP.rotationDegrees(rotX));
            poseStack.mulPose(Axis.YP.rotationDegrees(rotY));
            poseStack.mulPose(Axis.ZP.rotationDegrees(rotZ));
        }

        super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);

        poseStack.popPose();
    }
}
