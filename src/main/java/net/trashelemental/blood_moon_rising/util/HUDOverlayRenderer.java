package net.trashelemental.blood_moon_rising.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.trashelemental.blood_moon_rising.BloodMoonRising;

/**
 * This class handles rendering the 'broken heart' textures that show up when player
 * maximum health is reduced below 20.
 */
@Mod.EventBusSubscriber
public class HUDOverlayRenderer {

    @SubscribeEvent
    public static void onRenderGUI(RenderGuiOverlayEvent.Pre event) {
        if (Minecraft.getInstance().options.hideGui) return;
        if (event.getOverlay().id() != VanillaGuiOverlay.PLAYER_HEALTH.id()) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || player.isCreative() || player.isSpectator()) return;

        int maxHealth = (int) player.getAttributeValue(Attributes.MAX_HEALTH);
        int totalHearts = Math.min(10, maxHealth / 2);
        ResourceLocation BROKEN_HEART = new ResourceLocation(BloodMoonRising.MOD_ID, "textures/gui/gui_broken_heart.png");

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BROKEN_HEART);

        GuiGraphics gui = event.getGuiGraphics();
        int xBase = mc.getWindow().getGuiScaledWidth() / 2 - 91;
        int yBase = mc.getWindow().getGuiScaledHeight() - 39;


        for (int i = 0; i < 10; ++i) {
            if (i >= totalHearts) {
                gui.blit(BROKEN_HEART, xBase + i * 8, yBase, 0, 0, 9, 9, 9, 9);
            }
        }
    }

}
