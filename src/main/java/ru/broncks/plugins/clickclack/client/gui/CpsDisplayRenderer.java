package ru.broncks.plugins.clickclack.client.gui;

//? if >1.21.4 {
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
//?} else {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
 */
//?}

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

import ru.broncks.plugins.clickclack.config.ConfigManager;
import ru.broncks.plugins.clickclack.config.ModConfig;
import ru.broncks.plugins.clickclack.handler.ClickCounter;

public class CpsDisplayRenderer {
    private static final Identifier ID = Identifier.fromNamespaceAndPath("clickclack", "cps_display");

    public static void register() {
        //? if >1.21.4 {
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                ID,
                CpsDisplayRenderer::render
        );
        //?} else {
        /*HudRenderCallback.EVENT.register(
                ID,
                CpsDisplayRenderer::render
         );
         */
        //?}
    }

    public static void render(GuiGraphicsExtractor context, DeltaTracker tickCounter) {

        ModConfig config = ConfigManager.getConfig();
        if (!config.cps.enabled) return;

        Minecraft client = Minecraft.getInstance();

        if (/*? if >=26.2 {*/ client.gui.hud.isHidden() /*?} else {*//* client.options.hideGui *//*?}*/) return;

        int leftCps = ClickCounter.getLeftCps();
        // int rightCps = ClickCounter.getRightCps();
        String text = String.format("[CPS : %d ]", leftCps);

        Font textRenderer = client.font;
        int textWidth = textRenderer.width(text);
        int textHeight = textRenderer.lineHeight;
        int padding = 2;
        int totalWidth = textWidth + padding * 2;
        int totalHeight = textHeight + padding * 2;

        int x;
        if (config.cps.alignRight) {
            x = context.guiWidth() - totalWidth - config.cps.x;
        } else {
            x = config.cps.x;
        }
        int y = config.cps.y;

        context.fill(x,
                y,
                x + totalWidth,
                y + totalHeight,
                config.cps.backgroundColor);

        //? if new_render_api {
        context.text(client.font,
                text,
                x + padding,
                y + padding,
                config.cps.textColor,
                false);
        //?} else {
        /*context.drawString(client.font,
                text,
                x + padding,
                y + padding,
                config.cps.textColor,
                false);
        *///?}
    }
}