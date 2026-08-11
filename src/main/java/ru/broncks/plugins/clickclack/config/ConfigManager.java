package ru.broncks.plugins.clickclack.config;

import me.shedaniel.autoconfig.AutoConfig;
import ru.broncks.plugins.clickclack.Clickclack;

public class ConfigManager {
    public static ModConfig getConfig() {
        return (ModConfig)AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }

    public static void save() {
        try {
            AutoConfig.getConfigHolder(ModConfig.class).save();
        } catch (Throwable e) {
            Clickclack.LOGGER.error("Failed to save config", e);
        }
    }
}
