package edu.kit.riscjblockits.view.client.screens.widgets.text;

import net.minecraft.SharedConstants;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TextEditWidget implements Widget, Drawable, Element, Selectable {
    private static final int SCROLL_MULTIPLIER = 6;

    private static final float TEXT_SCALE = 0.9f;

    private static final float INVERSE_TEXT_SCALE = 1 / TEXT_SCALE;
    private static final int LINE_HEIGHT = 9;
    private List<Line> lines;
    private int scrollPosition = 0;
    private final TextRenderer textRenderer;

    private int x;
    private int y;
    private int width;
    private int height;
    private int tickCounter = 0;
    private int cursorX = 0;
    private int cursorY = 0;
    private int windowStartX = 0;

    public TextEditWidget(TextRenderer textRenderer, int x, int y, int width, int height) {
        this.textRenderer = textRenderer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setText("");
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int start = (int) Math.max(scrollPosition/(LINE_HEIGHT * TEXT_SCALE) - 1, 0);
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
        // draw lines with their index
        for (int i = start; i < lines.size(); i++) {
            int displayY = (int) (y * INVERSE_TEXT_SCALE + (i - start) * LINE_HEIGHT);
            drawLineIndex(context, i, displayY);
            context.drawText(textRenderer, lines.get(i).getContent(windowStartX), (int) (x * INVERSE_TEXT_SCALE), displayY, 0x000000, false);
            if ((i - start) * LINE_HEIGHT > height) {
                break;
            }
        }
        // draw cursor
        if (lines.get(cursorY).getContent().length() <= cursorX){
            drawCursor(context, (int) (x * INVERSE_TEXT_SCALE) + textRenderer.getWidth(lines.get(cursorY).getContentUntil(cursorX)),
                    (int) (y * INVERSE_TEXT_SCALE + (cursorY - start) * LINE_HEIGHT), true);
        }
        else 
            drawCursor(context, (int) (x * INVERSE_TEXT_SCALE) + textRenderer.getWidth(lines.get(cursorY).getContentUntil(cursorX)),
                (int) (y * INVERSE_TEXT_SCALE + (cursorY - start) * LINE_HEIGHT), false);

        matrixStack.pop();

    }

    private void drawLineIndex(DrawContext context, int i, int y) {
        int width = textRenderer.getWidth(String.valueOf(i));
        context.drawText(textRenderer, String.valueOf(i), (int) (x * INVERSE_TEXT_SCALE - width - 10), y, 0x000000, false);
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
        return (int) ((lines.size() + 5) * LINE_HEIGHT * TEXT_SCALE);
    }

    public void setText(String text) {
        this.lines = new ArrayList<>();
        for (String lineString : text.split("\n", -1)) {
            lines.add(new Line(lineString));
        }
        scrollPosition = 0;
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

    public String getText() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            result.append(lines.get(i).getContent());
        }
        return result.toString();
    }

    private void drawCursor(DrawContext context, int x, int y, boolean endOfLine) {
        if (this.tickCounter / 6 % 2 == 0) {
            context.drawText(this.textRenderer, endOfLine ? "_" : "|", x , y, endOfLine ? 0 : 0xAA777777, false);
        }
    }

    public boolean charTyped(char chr, int modifiers) {
        if (Element.super.charTyped(chr, modifiers)) {
            return true;
        } else if (SharedConstants.isValidChar(chr)) {
            insert(Character.toString(chr));
            return true;
        } else {
            return false;
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            case 257:
            case 335:
                insert("\n");
                return true;
            case 259:
                delete(-1);
                return true;
            case 261:
                delete(1);
                return true;
            case 262:
                moveCursorX(1);
                return true;
            case 263:
                moveCursorX(-1);
                return true;
            case 264:
                moveCursorY(1);
                return true;
            case 265:
                moveCursorY(-1);
                return true;
        }
        return false;
    }

    private void moveCursorX(int i) {
        // move out of left of line --> end of previous line
        if (cursorX + i < 0 && cursorY > 0) {
            cursorY--;
            cursorX = lines.get(cursorY).getContent().length();
            updateWindow();
            return;
        }
        // move out of the right if line --> start of next line
        if (cursorX + i > lines.get(cursorY).getContent().length() && cursorY < lines.size() - 1) {
            cursorY++;
            cursorX = 0;
            updateWindow();
            return;
        }
        cursorX = MathHelper.clamp(cursorX + i, 0, lines.get(cursorY).getContent().length());
        updateWindow();
    }

    private void moveCursorY(int i) {
        cursorY = MathHelper.clamp(cursorY + i, 0, lines.size() - 1);
        // adapt x to new lines constraints if needed
        cursorX = MathHelper.clamp(cursorX, 0, Math.max(lines.get(cursorY).getContent().length() - 1, 0));
        updateWindow();
    }

    private void insert(String text) {
        String[] splitString = text.split("\n", -1);

        String afterInsertContent = "";
        if (cursorX < lines.get(cursorY).getContent().length() - 1) {
            afterInsertContent = lines.get(cursorY).cut(cursorX, lines.get(cursorY).getContent().length());
        }
        for (int i = 0; i < splitString.length; i++) {
            int j = i + cursorY;

            // create new line if necessary
            if ( j >= lines.size()) {
                lines.add(new Line());
                cursorX = 0;
            }
            Line line = lines.get(j);
            line.insert(splitString[i], cursorX);
            cursorX = cursorX + splitString[i].length();

        }
        lines.get(cursorY + splitString.length - 1).insert(afterInsertContent, cursorX);
        cursorX += afterInsertContent.length();
        cursorY = cursorY + splitString.length - 1;
        updateWindow();
    }

    public void delete(int i) {


        int columnDelta = 0;
        int from;
        int to;
        // backspace
        if (i < 0) {
            if (cursorX == 0) {
                unhookLine(cursorY);
                return;
            }
            from = cursorX + i ;
            to = cursorX;
            columnDelta = i;
        }
        // delete
        else {
            if (lines.get(cursorY).getContent().length() == cursorX) {
                unhookLine(cursorY + 1);
                return;
            }
            to = cursorX + i ;
            from = cursorX ;
        }
        if (from < 0 || to > lines.get(cursorY).getContent().length())
            return;
        cursorX = cursorX + columnDelta;
        lines.get(cursorY).cut(from, to);
        updateWindow();

    }

    private void unhookLine(int line) {
        // cant unhook the only line
        if (lines.size() <= 1)
            return;
        if (line == 0)
            return;
        if (lines.size() <= line)
            return;
        lines.get(line-1).setContent(lines.get(line-1).getContent() + lines.get(line).getContent());
        lines.remove(line);
        // only change cursorY when the current line was removed --> also set x to end of line
        if (line == cursorY) {
            cursorY--;
            cursorX = lines.get(cursorY).getContent().length();
        }
        else
            cursorX = 0;

        if (cursorY < 0)
            cursorY = 0;

        updateWindow();
    }

    private void updateWindow() {
        // cursor going out the top
        if (cursorY * LINE_HEIGHT < scrollPosition)
            scrollPosition = cursorY * LINE_HEIGHT;

        // cursor going out the bottom
        if ((cursorY + 1) * LINE_HEIGHT > scrollPosition + height)
            scrollPosition = (cursorY + 1) * LINE_HEIGHT - height;

        // cursor going out of left side
        if (cursorX < windowStartX) {
            windowStartX = cursorX;
        }

        // cursor going out of right side
        if (cursorX > windowStartX + width)
            windowStartX = cursorX - width;
    }

}
