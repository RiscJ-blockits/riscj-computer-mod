package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.language.I18n;

public class CategoryEntry extends ListEntry{
    private String name;
    private String key;
    private ScrollableTextWidget textWidget;
    private TextRenderer textRenderer;

    private final int height = 20;
    private final int width = 100;
    public CategoryEntry(int x, int y, String name, String key, ScrollableTextWidget textWidget, TextRenderer textRenderer) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.key = key;
        this.textWidget = textWidget;
        this.textRenderer = textRenderer;

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        context.fill(x, y, x + width, y + height, 0x77000000);

        context.drawText(textRenderer, I18n.translate(name), x, y, 0xFFFFFF, false);


        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            select();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void select() {
        textWidget.setText(I18n.translate(key));
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
