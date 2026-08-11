package ru.broncks.plugins.clickclack.client.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import ru.broncks.plugins.clickclack.config.ConfigManager;
import ru.broncks.plugins.clickclack.config.ModConfig;
import ru.broncks.plugins.clickclack.client.gui.CpsPositionScreen;

public class ClickclackConfigScreen {
    public static Screen create(Screen parent) {
        ModConfig config = ConfigManager.getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("clickclack.gui.title").withStyle(ChatFormatting.BOLD, ChatFormatting.AQUA))
                .setSavingRunnable(ConfigManager::save);

        builder.setGlobalized(true);
        builder.setTransparentBackground(true);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // General:
        ConfigCategory general = builder.getOrCreateCategory(
                Component.translatable("clickclack.gui.category.general").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
        );

        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("clickclack.config.enable_mod"), config.general.enabled)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("clickclack.config.enable_mod.tooltip"))
                .setSaveConsumer(v -> config.general.enabled = v)
                .build());

        general.addEntry(entryBuilder.startIntField(
                        Component.translatable("clickclack.config.left_clicks_per_press"),
                        config.general.clicksPerPress)
                .setDefaultValue(3)
                .setMin(1)
                .setMax(40)
                .setTooltip(Component.translatable("clickclack.config.left_clicks_per_press.tooltip"))
                .setSaveConsumer(v -> config.general.clicksPerPress = v)
                .build()
        );

        general.addEntry(entryBuilder.startIntField(
                        Component.translatable("clickclack.config.delay_left_between_clicks"),
                        config.general.delayLeftBetweenClicks)
                .setDefaultValue(50)
                .setMin(0)
                .setMax(500)
                .setTooltip(Component.translatable("clickclack.config.delay_between_clicks.tooltip"))
                .setSaveConsumer(v -> config.general.delayLeftBetweenClicks = v)
                .build()
        );

        general.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("clickclack.config.remap_right_to_left"),
                        config.general.remapRightClickToLeft)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("clickclack.config.remap_right_to_left.tooltip"))
                .setSaveConsumer(v -> config.general.remapRightClickToLeft = v)
                .build()
        );

        general.addEntry(entryBuilder.startIntField(
                        Component.translatable("clickclack.config.right_clicks_per_press"),
                        config.general.rightClicksPerPress)
                .setDefaultValue(2)
                .setMin(1)
                .setMax(40)
                .setTooltip(Component.translatable("clickclack.config.right_clicks_per_press.tooltip"))
                .setSaveConsumer(v -> config.general.rightClicksPerPress = v)
                .build()
        );

        general.addEntry(entryBuilder.startIntField(
                        Component.translatable("clickclack.config.delay_right_between_clicks"),
                        config.general.delayRightBetweenClicks)
                .setDefaultValue(50)
                .setMin(0)
                .setMax(500)
                .setTooltip(Component.translatable("clickclack.config.delay_between_clicks.tooltip"))
                .setSaveConsumer(v -> config.general.delayRightBetweenClicks = v)
                .build()
        );

        // CPS:
        ConfigCategory cpsCategory = builder.getOrCreateCategory(
                Component.translatable("clickclack.gui.category.cps").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)
        );

        cpsCategory.addEntry(entryBuilder.startBooleanToggle(
                        Component.translatable("clickclack.config.enable_cps_counter"),
                        config.cps.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(v -> config.cps.enabled = v)
                .build()
        );

        cpsCategory.addEntry(entryBuilder.startColorField(
                        Component.translatable("clickclack.config.text_color"),
                        config.cps.textColor)
                .setDefaultValue(0xFFFFFFFF)
                .setAlphaMode(true)
                .setSaveConsumer(v -> config.cps.textColor = v)
                .build()
        );

        cpsCategory.addEntry(entryBuilder.startColorField(
                        Component.translatable("clickclack.config.background_color"),
                        config.cps.backgroundColor)
                .setDefaultValue(0x80000000)
                .setAlphaMode(true)
                .setSaveConsumer(v -> config.cps.backgroundColor = v)
                .build()
        );

        cpsCategory.addEntry(new ButtonConfigEntry(
                        Component.translatable("clickclack.gui.adjust_position"),
                        Component.translatable("clickclack.gui.open_editor"),
                        button -> /*? if >=26.2 {*/ Minecraft.getInstance().gui.setScreen(new CpsPositionScreen(Minecraft.getInstance().gui.screen())) /*?} else {*//* Minecraft.getInstance().setScreen(new CpsPositionScreen(Minecraft.getInstance().screen)) *//*?}*/
                )
        );

        return builder.build();
    }
}