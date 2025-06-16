package net.trashelemental.blood_moon_rising.blood_moon.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.trashelemental.blood_moon_rising.Config;
import net.trashelemental.blood_moon_rising.blood_moon.BloodMoonClientEvents;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LightTexture.class)
public abstract class LightMapMixin {

    static {
        System.out.println("[BloodMoonRising] LightMapMixin has been loaded!");
    }

    @Inject(
            method = "updateLightTexture",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/LightTexture;blockLightRedFlicker:F"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void tintSkyLightRed(float partialTicks, CallbackInfo ci, ClientLevel clientLevel, float f, float f1, float f2, float f3, float f4, float f6, float f5, Vector3f skyVector) {
        if (BloodMoonClientEvents.isBloodMoonActive() && Config.DO_BLOOD_MOON_VISUAL_EFFECTS.get()) {
            float redTint = 1.2f;
            float greenTint = 0.4f;
            float blueTint = 0.4f;
            skyVector.mul(redTint, greenTint, blueTint);
        }
    }

}