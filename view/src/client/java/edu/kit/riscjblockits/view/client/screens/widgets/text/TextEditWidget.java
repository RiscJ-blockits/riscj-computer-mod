package edu.kit.riscjblockits.view.client.screens.widgets.text;

import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
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

    private static final float TEXT_SCALE = 1f;

    private static final float INVERSE_TEXT_SCALE = 1 / TEXT_SCALE;
    private static final int LINE_HEIGHT = 9;
    private static final int TAB_SPACE_COUNT = 4;
    private static final int SELECTION_COLOR = 0x99FF0000;
    private static final int TEXT_COLOR = 0x000000;
    private List<Line> lines;
    private int scrollPosition = 0;
    private final TextRenderer textRenderer;

    private int x;
    private int y;
    private int width;
    private int height;
    private float tickCounter = 0;
    private int cursorX = 0;
    private int cursorY = 0;
    private int windowStartX = 0;
    private boolean selecting = false;
    private int selectionEnd = 0;
    private int selectionStart = 0;
    private int windowStartTextIndex = 0;

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
        tickCounter += delta;
        // calculate start index of text
        int start = (int) Math.max(scrollPosition/(LINE_HEIGHT * TEXT_SCALE) - 1, 0);
        int currentTextIndex = windowStartTextIndex;
        // prepare text scaling
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
        // draw lines with their index
        for (int i = start; i < lines.size(); i++) {
            if ((i - start) * LINE_HEIGHT >= height) {
                break;
            }
            int displayY = (int) (y * INVERSE_TEXT_SCALE + (i - start) * LINE_HEIGHT);
            // draw selection if current line is in selection
            if (selectionStart != selectionEnd && currentTextIndex >= selectionStart && currentTextIndex <= selectionEnd) {
                String selectedText = getSelectedText(i, currentTextIndex);
                int selectionStartInLine = textRenderer.getWidth(
                        lines.get(i).getContent().substring(0, Math.max(0, selectionStart - currentTextIndex))
                );
                drawSelection(context, (int) (x * INVERSE_TEXT_SCALE) + selectionStartInLine, displayY, textRenderer.getWidth(selectedText));
            }
            // draw line index
            drawLineIndex(context, i, displayY);
            // draw line content
            context.drawText(textRenderer, lines.get(i).getContent(windowStartX), (int) (x * INVERSE_TEXT_SCALE), displayY, TEXT_COLOR, false);

            // increment text index
            currentTextIndex += lines.get(i).getContent().length();
        }
        // draw cursor, different mode if cursor is at the end of the line
        if (lines.get(cursorY).getContent().length() <= cursorX){
            drawCursor(context,
                    (int) (x * INVERSE_TEXT_SCALE) + textRenderer.getWidth(lines.get(cursorY).getContentUntil(cursorX).substring(windowStartX)),
                    (int) (y * INVERSE_TEXT_SCALE + (cursorY - start) * LINE_HEIGHT), true);
        }
        else
            drawCursor(context,
                    (int) (x * INVERSE_TEXT_SCALE) + textRenderer.getWidth(lines.get(cursorY).getContentUntil(cursorX).substring(windowStartX)),
                    (int) (y * INVERSE_TEXT_SCALE + (cursorY - start) * LINE_HEIGHT), false);

        matrixStack.pop();

    }

    private String getSelectedText(int i, int currentTextIndex) {
        String selectedText = lines.get(i).getContent();
        // cut off selection in line before start
        if (currentTextIndex < selectionStart) {
            selectedText = selectedText.substring(currentTextIndex - selectionStart);
        }
        // cut off selection in line after end
        if (currentTextIndex + selectedText.length() > selectionEnd) {
            selectedText = selectedText.substring(0, selectionEnd - currentTextIndex);
        }
        return selectedText;
    }

    private void drawLineIndex(DrawContext context, int i, int y) {
        int width = textRenderer.getWidth(String.valueOf(i));
        context.drawText(textRenderer, String.valueOf(i), (int) (x * INVERSE_TEXT_SCALE - width - 10), y, TEXT_COLOR, false);
    }

    private void drawSelection(DrawContext context, int x, int y, int width) {
        context.fill(x, y, x + width, y + LINE_HEIGHT, SELECTION_COLOR);
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
            if (i < lines.size() + 1)
                result.append("\n");
        }
        return result.toString();
    }

    private void drawCursor(DrawContext context, int x, int y, boolean endOfLine) {
        if (((int) this.tickCounter) / 6 % 2 == 0) {
            context.drawText(this.textRenderer, endOfLine ? "_" : "|", x , y, endOfLine ? 0 : 0x999999, false);
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
        this.selecting = Screen.hasShiftDown();
        if (Screen.isSelectAll(keyCode)) {
            setSelection(0, getText().length());
            return true;
        } else if (Screen.isCopy(keyCode)) {
            MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
            return true;
        } else if (Screen.isPaste(keyCode)) {
            this.insert(MinecraftClient.getInstance().keyboard.getClipboard());
            return true;
        } else if (Screen.isCut(keyCode)) {
            MinecraftClient.getInstance().keyboard.setClipboard(this.getSelectedText());
            this.insert("");
            return true;
        } else {
            return switch (keyCode) {
                case 257, 335 -> {      // enter
                    insert("\n");
                    yield true;
                }
                case 259 -> {           // backspace
                    if (selectionStart != selectionEnd) {
                        insert("");
                    } else {
                        delete(-1);
                    }
                    yield true;
                }
                case 261 -> {           // delete
                    if (selectionStart != selectionEnd) {
                        insert("");
                    } else {
                        delete(1);
                    }
                    yield true;
                }
                case 262 -> {           // right arrow
                    moveCursorX(1);
                    yield true;
                }
                case 263 -> {           // left arrow
                    moveCursorX(-1);
                    yield true;
                }
                case 264 -> {           // down arrow
                    moveCursorY(1);
                    yield true;
                }
                case 265 -> {           // up arrow
                    moveCursorY(-1);
                    yield true;
                }
                case 258 -> {           // tab
                    insert(" ".repeat(TAB_SPACE_COUNT));
                    yield true;
                }
                default -> false;
            };
        }
    }

    private String getSelectedText() {
        int start = Math.min(selectionStart, selectionEnd);
        int end = Math.max(selectionStart, selectionEnd);
        return getText().substring(start, end);
    }

    private void setSelection(int i, int length) {
        this.selectionStart = i;
        this.selectionEnd = i + length;
    }

    private void moveCursorX(int i) {
        // update selection if selecting
        if (selecting) {

            // init selection if there is none
            if (selectionStart == selectionEnd) {
                selectionStart = getTextToCursor();
                selectionEnd = getTextToCursor();
            }

            if (i < 0) {
                selectionStart = Math.max(0, selectionStart + i);
            } else {
                selectionEnd = Math.min(getText().length(), selectionEnd + i);
            }
        } else if (selectionStart != selectionEnd) {
            selectionStart = 0;
            selectionEnd = 0;
            return;
        }
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
        // simply move in line
        cursorX = MathHelper.clamp(cursorX + i, 0, lines.get(cursorY).getContent().length());
        updateWindow();
    }

    private int getTextToCursor() {
        int textTillHere = 0;
        for (int i = 0; i < cursorY; i++) {
            textTillHere += lines.get(i).getContent().length();
        }
        return textTillHere + cursorX;
    }

    private void moveCursorY(int i) {
        // update selection if selecting
        if (selecting) {
            if (i < 0) {
                int lineLengthSum = 0;
                for (int j = cursorY; j > cursorY + i; j--) {
                    lineLengthSum += lines.get(j).getContent().length();
                }
                selectionStart = Math.max(0, selectionStart + i - lineLengthSum);
            } else {
                int lineLengthSum = 0;
                for (int j = cursorY; j < cursorY + i; j++) {
                    lineLengthSum += lines.get(j).getContent().length();
                }
                selectionEnd = Math.min(getText().length(), selectionEnd + i + lineLengthSum);
            }
        }
        // move line
        cursorY = MathHelper.clamp(cursorY + i, 0, lines.size() - 1);
        // adapt x to new lines constraints if needed
        cursorX = MathHelper.clamp(cursorX, 0, Math.max(lines.get(cursorY).getContent().length(), 0));
        updateWindow();
    }

    private void insert(String text) {
        // delete selection if something is selected
        if (selectionStart != selectionEnd) {
            deleteSelection();
        }

        String[] splitString = text.split("\n", -1);

        String afterInsertContent = "";
        if (cursorX < lines.get(cursorY).getContent().length() - 1) {
            afterInsertContent = lines.get(cursorY).cut(cursorX, lines.get(cursorY).getContent().length());
        }
        for (int i = 0; i < splitString.length; i++) {
            int j = i + cursorY;

            // create new line if necessary
            if ( i >= 1) {
                if (j >= lines.size())
                    lines.add(new Line());
                else
                    lines.add(j, new Line());
                cursorX = 0;
            }
            Line line = lines.get(j);
            line.insert(splitString[i], cursorX);
            moveCursorX(splitString[i].length());

        }
        lines.get(cursorY + splitString.length - 1).insert(afterInsertContent, cursorX);
        moveCursorY(splitString.length - 1);
        updateWindow();
    }

    private void deleteSelection() {
        int textTillHere = 0;
        List<Integer> linesToDelete = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            // delete selection in line if line is in selection
            if (textTillHere + line.getContent().length() >= selectionStart) {
                int start = Math.max(0, selectionStart - textTillHere);
                int end = Math.min(line.getContent().length(), selectionEnd - textTillHere);
                line.cut(start, end);
                // adapt cursor if line is the current line
                if (i == cursorY) {
                    cursorX = start;
                }
                // remove line if selection goes over multiple lines
                if (selectionEnd - textTillHere > line.getContent().length()) {
                    linesToDelete.add(i);
                }

            }
            // update textTillHere
            textTillHere += line.getContent().length();
            if (textTillHere >= selectionEnd) {
                break;
            }
        }
        // remove lines that are not needed anymore
        for (int i = linesToDelete.size() - 1; i >= 0; i--) {
            lines.remove((int) linesToDelete.get(i));
        }
        selectionStart = 0;
        selectionEnd = 0;
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
            moveCursorY(-1);
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
        if ((cursorY + 1) * LINE_HEIGHT < scrollPosition) {
            // calculate the length of the lines that are not displayed
            int lineLengthSum = 0;
            for (int j = cursorY; j > (scrollPosition / LINE_HEIGHT -cursorY); j--) {
                lineLengthSum += lines.get(j).getContent().length();
            }
            windowStartTextIndex -= lineLengthSum;
            scrollPosition = (cursorY + 1) * LINE_HEIGHT;
        }

        // cursor going out the bottom
        if (cursorY + 1 > (scrollPosition + height) / LINE_HEIGHT) {
            // calculate the length of the lines that are not displayed
            int lineLengthSum = 0;
            for (int j = cursorY; j < ((scrollPosition + height) / LINE_HEIGHT - cursorY); j++) {
                lineLengthSum += lines.get(j).getContent().length();
            }
            windowStartTextIndex += lineLengthSum;

            scrollPosition = ((cursorY + 1) * LINE_HEIGHT) - height;
        }

        // cursor going out of left side
        int totalCharacters = lines.get(cursorY).getContent().length();
        if (cursorX <= windowStartX) {
            windowStartX = MathHelper.clamp(cursorX - 1, 0, totalCharacters);
        }

        // cursor going out of right side
        int shownCharacters = textRenderer.trimToWidth(lines.get(cursorY).getContent().substring(windowStartX), width).length();
        if (cursorX > windowStartX + shownCharacters)
            windowStartX = cursorX - shownCharacters;
    }

}
