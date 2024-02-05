package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class ScrollableTextWidget implements Widget, Drawable, Element, Selectable {
    private static final int SCROLL_MULTIPLIER = 6;
    private static final int LINE_HEIGHT = 10;
    private List<Text> lines;
    private int scrollPosition = 0;
    private final TextRenderer textRenderer;

    private int x;
    private int y;
    private int width;
    private int height;

    public ScrollableTextWidget(TextRenderer textRenderer, int x, int y, int width, int height) {
        this.textRenderer = textRenderer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setText("");
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int start = Math.max(scrollPosition/LINE_HEIGHT - 1, 0);
        for (int i = start; i < lines.size(); i++) {
            context.drawText(textRenderer,lines.get(i), x, y + (i - start) * LINE_HEIGHT, 0xFFFFFF, false);
            if ((i - start) * LINE_HEIGHT > height) {
                break;
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollPosition -= (int) (SCROLL_MULTIPLIER * verticalAmount);
        if (scrollPosition < 0) {
            scrollPosition = 0;
        } else if (scrollPosition > getContentsHeight() - height){
            scrollPosition = getContentsHeight() - height;
        }
        return Element.super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return Widget.super.getNavigationFocus();
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {

    }

    private int getContentsHeight() {
        return lines.size() * LINE_HEIGHT;
    }

    public void setText(String text) {
        this.lines = wrapText(text);
        scrollPosition = 0;
    }

    /**
     * Wraps the given text into lines that fit into the given width.
     * partially written by Github Copilot
     *
     * @param text The text to wrap.
     * @return A list of texts that fit into the given width.
     */

    private List<Text> wrapText(String text) {
        List<Text> result = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            // handle line breaks in text
            if (word.contains("\n")) {
                String[] split = word.split("\n", -1);
                for (int i = 0; i < split.length; i++) {
                    if (i != 0) {
                        result.add(Text.of(line.toString()));
                        line = new StringBuilder(split[i]);
                    }
                    else {
                        line.append(" ");
                        line.append(split[i]);
                        result.add(Text.of(line.toString()));
                        line = new StringBuilder();

                    }

                }
                continue;
            }
            //
            if (textRenderer.getWidth(line + " " + word) > width) {
                result.add(Text.of(line.toString()));
                line = new StringBuilder(word);
            } else {
                if (!line.isEmpty()) {
                    line.append(" ");
                }
                line.append(word);
            }
        }
        result.add(Text.of(line.toString()));
        return result;
    }

    @Override
    public SelectionType getType() {
        return SelectionType.HOVERED;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
