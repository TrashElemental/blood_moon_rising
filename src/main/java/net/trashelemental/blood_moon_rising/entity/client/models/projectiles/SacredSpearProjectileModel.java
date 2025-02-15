package net.trashelemental.blood_moon_rising.entity.client.models.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.entity.custom.LeechEntity;
import net.trashelemental.blood_moon_rising.entity.custom.projectiles.SacredSpearProjectileEntity;
import software.bernie.geckolib.model.GeoModel;

public class SacredSpearProjectileModel extends GeoModel<SacredSpearProjectileEntity> {
    @Override
    public ResourceLocation getModelResource(SacredSpearProjectileEntity animatable) {
        return BloodMoonRising.prefix("geo/models/projectiles/sacred_spear_projectile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SacredSpearProjectileEntity animatable) {

        return BloodMoonRising.prefix("textures/projectile/sacred_spear_projectile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SacredSpearProjectileEntity animatable) {
        return BloodMoonRising.prefix("animations/sacred_spear_projectile.animation.json");
    }

}