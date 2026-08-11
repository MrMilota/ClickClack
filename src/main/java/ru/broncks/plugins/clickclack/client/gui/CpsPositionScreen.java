package ru.broncks.plugins.clickclack.client.gui;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
//? if new_input_api {
import net.minecraft.client.input.MouseButtonEvent;
import org.jetbrains.annotations.NotNull;
//?}

import ru.broncks.plugins.clickclack.config.ConfigManager;
import ru.broncks.plugins.clickclack.config.ModConfig;

public class CpsPositionScreen extends Screen {
    private final Screen parent;
    private boolean dragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public CpsPositionScreen(Screen parent) {
        super(Component.translatable("clickclack.gui.cps_position"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(Component.translatable("clickclack.gui.done"), button -> {
                    ConfigManager.save();
                    /*? if >=26.2 {*/ this.minecraft.gui.setScreen(parent); /*?} else {*//* this.minecraft.setScreen(parent); *//*?}*/
                })
                .bounds(this.width / 2 - 100, this.height - 30, 200, 20)
                .build());
    }

    @Override
    public void onClose() {
        /*? if >=26.2 {*/ this.minecraft.gui.setScreen(parent); /*?} else {*//* this.minecraft.setScreen(parent); *//*?}*/
    }

    @Override
    //? if new_render_api {
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xA0000000);
        context.text(this.font, Component.translatable("clickclack.gui.cps_position.hint"), this.width / 2, 40, 0xFFFFFF);

        CpsDisplayRenderer.render(context, DeltaTracker.ZERO);

        super.extractRenderState(context, mouseX, mouseY, delta);
    }
    //?} else {
    /*public void render(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xA0000000);
        context.drawString(this.font, Component.translatable("clickclack.gui.cps_position.hint"), this.width / 2, 40, 0xFFFFFF);

        CpsDisplayRenderer.render(context, DeltaTracker.ZERO);

        super.render(context, mouseX, mouseY, delta);
    }
    *///?}

    //? if new_input_api {
    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if (click.button() == 0) {
            if (clickclack$tryStartDrag(click.x(), click.y())) return true;
        }
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(@NotNull MouseButtonEvent click) {
        dragging = false;
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseDragged(@NotNull MouseButtonEvent click, double deltaX, double deltaY) {
        if (dragging) {
            clickclack$applyDrag(click.x(), click.y());
            return true;
        }
        return super.mouseDragged(click, deltaX, deltaY);
    }
    //?} else {
    /*@Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (clickclack$tryStartDrag(mouseX, mouseY)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging) {
            clickclack$applyDrag(mouseX, mouseY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    */
    //?}

    private boolean clickclack$tryStartDrag(double clickX, double clickY) {
        ModConfig.Cps config = ConfigManager.getConfig().cps;

        String text = "[CPS : 0 ]";
        Font textRenderer = this.minecraft.font;
        int textWidth = textRenderer.width(text);
        int textHeight = textRenderer.lineHeight;
        int padding = 2;
        int totalWidth = textWidth + padding * 2;
        int totalHeight = textHeight + padding * 2;

        int currentX;
        if (config.alignRight) {
            currentX = this.width - totalWidth - config.x;
        } else {
            currentX = config.x;
        }
        int currentY = config.y;

        if (clickX >= currentX && clickX <= currentX + totalWidth &&
                clickY >= currentY && clickY <= currentY + totalHeight) {
            dragging = true;
            dragOffsetX = (int) (clickX - currentX);
            dragOffsetY = (int) (clickY - currentY);
            return true;
        }
        return false;
    }

    private void clickclack$applyDrag(double clickX, double clickY) {
        ModConfig.Cps config = ConfigManager.getConfig().cps;
        String text = "[CPS : 0 ]";

        Font textRenderer = this.minecraft.font;
        int textWidth = textRenderer.width(text);
        int padding = 2;
        int totalWidth = textWidth + padding * 2;

        int newX = (int) (clickX - dragOffsetX);
        int newY = (int) (clickY - dragOffsetY);

        if (newX + totalWidth / 2 > this.width / 2) {
            config.alignRight = true;
            config.x = this.width - totalWidth - newX;
        } else {
            config.alignRight = false;
            config.x = newX;
        }
        config.y = newY;
    }
}