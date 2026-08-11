package ru.broncks.plugins.clickclack.client;

/*? if >=1.21.11 {*/
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
/*?}*/

//? if new_render_api {
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
//?} else {
/*import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
 */
//?}
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.network.chat.Component;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import org.lwjgl.glfw.GLFW;
import ru.broncks.plugins.clickclack.client.gui.CpsDisplayRenderer;
import ru.broncks.plugins.clickclack.config.ConfigManager;
import ru.broncks.plugins.clickclack.config.ModConfig;

public class ClickclackClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CpsDisplayRenderer.register();

        //? if new_render_api {
        ToggleKeyMapping toggleKey = new ToggleKeyMapping(
                "key.clickclack.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KeyMapping.Category.register(Identifier.fromNamespaceAndPath("clickclack", "clickclack")),
                () -> false,
                true
        );
        KeyMappingHelper.registerKeyMapping(toggleKey);
        //?} else if =1.21.11 {
        /*ToggleKeyMapping toggleKey = new ToggleKeyMapping(
                "key.clickclack.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                KeyMapping.Category.register(Identifier.fromNamespaceAndPath("clickclack", "clickclack")),
                () -> false,
                true
        );
        KeyBindingHelper.registerKeyBinding(toggleKey);
         */
        //?} else if <=1.21.8 {
        /*ToggleKeyMapping toggleKey = new ToggleKeyMapping(
                "key.clickclack.toggle",
                GLFW.GLFW_KEY_UNKNOWN,
                "key.categories.misc",
                () -> false
        );
        KeyBindingHelper.registerKeyBinding(toggleKey);
        *///?}

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (toggleKey.consumeClick()) {
                ModConfig config = ConfigManager.getConfig();
                config.general.enabled = !config.general.enabled;
                ConfigManager.save();

                if (client.player != null) {
                    String status = config.general.enabled ? "clickclack.message.enabled" : "clickclack.message.disabled";
                    //? if new_render_api {
                    client.player.sendSystemMessage(
                            Component.translatable(status)
                    );
                    //?} else {
                    /*client.player.displayClientMessage(
                            Component.translatable(status), true
                    );
                    *///?}
                }
            }
        });
    }
}