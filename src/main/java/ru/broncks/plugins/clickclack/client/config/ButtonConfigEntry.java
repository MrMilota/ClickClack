package ru.broncks.plugins.clickclack.client.config;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ButtonConfigEntry extends AbstractConfigListEntry<Object> {
    private final Button button;
    private final Component fieldName;

    public ButtonConfigEntry(Component fieldName, Component buttonText, Button.OnPress onPress) {
        super(fieldName, false);
        this.fieldName = fieldName;
        this.button = Button.builder(buttonText, onPress)
                .bounds(0, 0, 150, 20)
                .build();
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public Optional<Object> getDefaultValue() {
        return Optional.empty();
    }

    @Override
    public void save() {
        // No-op
    }

    @Override
    //? if new_render_api {
    public void extractRenderState(GuiGraphicsExtractor context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        super.extractRenderState(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
        context.text(Minecraft.getInstance().font, this.fieldName, x, y + 6, 0xFFFFFF);

        this.button.setX(x + entryWidth - 150);
        this.button.setY(y);
        this.button.setWidth(150);
        this.button.extractRenderState(context, mouseX, mouseY, tickDelta);
    }
    //?} else {
    /*public void render(GuiGraphicsExtractor context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        super.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, hovered, tickDelta);
        context.drawString(Minecraft.getInstance().font, this.fieldName, x, y + 6, 0xFFFFFF);

        this.button.setX(x + entryWidth - 150);
        this.button.setY(y);
        this.button.setWidth(150);
        this.button.render(context, mouseX, mouseY, tickDelta);
    }
    *///?}

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return Collections.singletonList(button);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return Collections.singletonList(button);
    }
}
