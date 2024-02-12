package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class CategoryEntry extends ListEntry{

    private static final float MAX_TEXT_WIDTH = 70f;

    private final String name;
    private final String key;
    private final ScrollableTextWidget textWidget;
    private final TextRenderer textRenderer;
    private final int height = 20;
    private final int width = 100;
    private ItemStack icon;
    public CategoryEntry(int x, int y, String name, String key, ScrollableTextWidget textWidget, TextRenderer textRenderer) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.key = key;
        this.textWidget = textWidget;
        this.textRenderer = textRenderer;
    }

    public CategoryEntry(int x, int y, String name, String key, ScrollableTextWidget textWidget, TextRenderer textRenderer, ItemStack icon) {
        this(x, y, name, key, textWidget, textRenderer);
        this.icon = icon;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        context.fill(x, y, x + width, y + height, 0x77000000);

        String text = I18n.translate(name);

        float scale = Math.min(MAX_TEXT_WIDTH / textRenderer.getWidth(text), 1f);
        float inverseScale = 1 / scale;
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.scale(scale, scale, scale);

        context.drawText(textRenderer, text, (int) ((x + 3) * inverseScale), (int) ((y + 6)*inverseScale), 0xFFFFFF, false);

        matrixStack.pop();


        if (icon != null) {
            context.drawItem(icon, x + width - 18, y + 2);
        }

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
