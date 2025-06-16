package net.trashelemental.blood_moon_rising.blood_moon.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.trashelemental.blood_moon_rising.BloodMoonRising;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.blood_moon.BloodMoonClientEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public abstract class BloodMoonSkyMixin {

    static {
        System.out.println("[BloodMoonRising] BloodMoonSkyMixin has been loaded!");
    }

    private static final ResourceLocation BLOOD_MOON_LOCATION =
            new ResourceLocation(BloodMoonRising.MOD_ID, "textures/environment/blood_moon_phases.png");

    @Shadow
    @Final
    private static ResourceLocation MOON_LOCATION;

    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V",
                    ordinal = 1
            )
    )
    private void redirectMoonTexture(int textureUnit, ResourceLocation original) {
        if (BloodMoonClientEvents.isBloodMoonActive() && Config.DO_BLOOD_MOON_VISUAL_EFFECTS.get()) {
            RenderSystem.setShaderTexture(textureUnit, BLOOD_MOON_LOCATION);
        } else {
            RenderSystem.setShaderTexture(textureUnit, MOON_LOCATION);
        }
    }

}
