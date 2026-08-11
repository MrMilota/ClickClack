package ru.broncks.plugins.clickclack;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.broncks.plugins.clickclack.config.ModConfig;

public class Clickclack implements ModInitializer {
    public static final String MOD_ID = "clickclack";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing ClickClack");

        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
    }
}
