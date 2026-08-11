package ru.broncks.plugins.clickclack.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
/*? if new_input_api {*/ import net.minecraft.client.input.MouseButtonInfo; /*?}*/

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ru.broncks.plugins.clickclack.config.ConfigManager;
import ru.broncks.plugins.clickclack.config.ModConfig;
import ru.broncks.plugins.clickclack.handler.ClickHandler;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    //? if new_input_api {
    @SuppressWarnings("UnresolvedMixinReference")
    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    private void clickclack$onButton(final long handle, final MouseButtonInfo rawButtonInfo, final @MouseButtonInfo.Action int action, CallbackInfo ci) {
        if (action != GLFW.GLFW_PRESS) return;
        if /*? if >=26.2 {*/ (Minecraft.getInstance().gui.screen() != null) /*?} else {*//* (Minecraft.getInstance().screen != null) *//*?}*/  return;

        ModConfig.General config = ConfigManager.getConfig().general;
        if (!config.enabled) return;

        int button = rawButtonInfo.button();
        clickclack$handle(button, config, ci);
    }
    //?} else {
        /*@SuppressWarnings("UnresolvedMixinReference")
        @Inject(method = "onPress", at = @At("HEAD"), cancellable = true)
        private void clickclack$onButton(long window, int button, int action, int mods, CallbackInfo ci) {
            if (action != GLFW.GLFW_PRESS) return;
            if (Minecraft.getInstance().screen != null) return;

            ModConfig.General config = ConfigManager.getConfig().general;
            if (!config.enabled) return;

            clickclack$handle(button, config, ci);
        }
        */
    //?}

    @Unique
    private void clickclack$handle(int button, ModConfig.General config, CallbackInfo ci) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) { // лкм
            ClickHandler.onLeftClick();
            ci.cancel();
        } else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && config.remapRightClickToLeft) { // пкм ес нада
            ClickHandler.onRemappedRightClick();
            ci.cancel();
        }
    }
}