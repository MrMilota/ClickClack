    package ru.broncks.plugins.clickclack.config;

    import me.shedaniel.autoconfig.ConfigData;
    import me.shedaniel.autoconfig.annotation.Config;
    import me.shedaniel.autoconfig.annotation.ConfigEntry;
    import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

    @Config(name = "clickclack")
    public class ModConfig implements ConfigData {
        @ConfigEntry.Category("general")
        @ConfigEntry.Gui.TransitiveObject
        public ModConfig.General general = new ModConfig.General();

        @ConfigEntry.Category("cps")
        @ConfigEntry.Gui.TransitiveObject
        public ModConfig.Cps cps = new ModConfig.Cps();

        public static class General {
            @Comment("Включить работу мода")
            @ConfigEntry.Gui.Tooltip
            public boolean enabled = true;

            @Comment("Количество левых кликов за одно нажатие ЛКМ (1 = выключено)")
            public int clicksPerPress = 3;

            @Comment("Задержка ЛКМ между кликами в миллисекундах")
            public int delayLeftBetweenClicks = 50;

            @Comment("Переназначать ПКМ на ЛКМ (вместо использования предмета/размещения блока)")
            public boolean remapRightClickToLeft = false;

            @Comment("Количество левых кликов при нажатии ПКМ (если remapRightClickToLeft = true)")
            public int rightClicksPerPress = 2;

            @Comment("Задержка ЛКМ между кликами в миллисекундах")
            public int delayRightBetweenClicks = 50;
        }

        public static class Cps implements ConfigData {
            @Comment("Показывать CPS на экране")
            @ConfigEntry.Gui.Tooltip
            public boolean enabled = true;

            @Comment("Позиция по X")
            public int x = 0;

            @Comment("Позиция по Y")
            public int y = 0;

            @Comment("Выравнивание вправо")
            public boolean alignRight = false;

            @Comment("Цвет текста (HEX)")
            @ConfigEntry.ColorPicker(allowAlpha = true)
            public int textColor = -1;

            @Comment("Цвет фона (HEX)")
            @ConfigEntry.ColorPicker(allowAlpha = true)
            public int backgroundColor = Integer.MIN_VALUE;

            @Override
            public void validatePostLoad() {
                if ((textColor >>> 24) == 0) {
                    textColor |= 0xFF000000;
                }
            }
        }
    }