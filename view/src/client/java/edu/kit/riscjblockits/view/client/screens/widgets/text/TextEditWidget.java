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
    protected static final int LINE_HEIGHT = 9;
    private static final int TAB_SPACE_COUNT = 4;
    protected static final int SELECTION_COLOR = 0x99FF0000;
    protected static final int TEXT_COLOR = 0x000000;
    private List<Line> lines;
    private int scrollPosition = 0;
    protected final TextRenderer textRenderer;

    private int x;
    private int y;
    private int width;
    private int height;
    private float tickCounter = 0;
    private int cursorX = 0;
    private int cursorY = 0;
    private int windowStartX = 0;
    private boolean selecting = false;
    private int selectionEndX = 0;
    private int selectionEndY = 0;
    private int windowStartTextIndex = 0;
    private boolean hasSelected = false;

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
            if (hasSelected) {
                drawSelectionForLine(context, i, displayY);
            }
            // draw line index
            drawLineIndex(context, i, displayY);
            // draw line content
            drawLine(context, lines.get(i).getContent(), windowStartX, i, (int) (x * INVERSE_TEXT_SCALE), displayY);
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

    protected void drawLine(DrawContext context, String line, int windowStartX, int i, int displayX, int displayY) {
        context.drawText(textRenderer, line.substring(windowStartX), displayX, displayY, TEXT_COLOR, false);
    }

    protected void drawSelectionForLine(DrawContext context, int i, int displayY) {
        int startX;
        int startY;
        int endX;
        int endY;
        if (cursorY == selectionEndY) {
            startY = cursorY;
            endY = cursorY;
            startX = Math.min(cursorX, selectionEndX);
            endX = Math.max(cursorX, selectionEndX);
        }
        else if (cursorY > selectionEndY) {
            startY = selectionEndY;
            endY = cursorY;
            startX = selectionEndX;
            endX = cursorX;
        } else {
            startY = cursorY;
            endY = selectionEndY;
            startX = cursorX;
            endX = selectionEndX;
        }


        String selectedText = getLineSelection(i, startX, startY, endX, endY);
        int charsBefore = startY == i ? startX : 0;                                    // start of selection in line
        charsBefore = endY == i ? endX - selectedText.length() : charsBefore; // end of selection in line
        // calculate width of text before selection --> draw selection offset by that width
        String wholeLine = lines.get(i).getContent();
        int beforeWidth;
        if (wholeLine.length() >= windowStartX && charsBefore >= windowStartX)
            beforeWidth = textRenderer.getWidth(wholeLine.substring(windowStartX, charsBefore));
        else
            beforeWidth = textRenderer.getWidth(wholeLine.substring(0, charsBefore));
        drawSelection(context, (int) (x * INVERSE_TEXT_SCALE) + beforeWidth, displayY, textRenderer.getWidth(selectedText));
    }

    private String getLineSelection(int i, int startX, int startY, int endX, int endY) {
        String selectedText = "";
        // single line selection
        if (i == startY && i == endY) {
            selectedText = lines.get(i).getContent().substring(startX, endX);
        }
        // start of selection --> cut off text before start
        else if (i == startY) {
            selectedText = lines.get(i).getContent().substring(startX);
        }
        // end of selection --> cut off text after end
        else if (i == endY) {
            selectedText = lines.get(i).getContent().substring(0, endX);
        }
        // middle of selection --> select whole line
        else if (i > startY && i < endY) {
            selectedText = lines.get(i).getContent();
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
            this.selectionEndX = 0;
            this.selectionEndY = 0;
            this.cursorY = lines.size() - 1;
            this.cursorX = lines.get(cursorY).getContent().length();
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
                    if (hasSelected) {
                        insert("");
                    } else {
                        delete(-1);
                    }
                    yield true;
                }
                case 261 -> {           // delete
                    if (hasSelected) {
                        insert("");
                    } else {
                        delete(1);
                    }
                    yield true;
                }
                case 262 -> {           // right arrow
                    moveCursorX(1, false);
                    yield true;
                }
                case 263 -> {           // left arrow
                    moveCursorX(-1, false);
                    yield true;
                }
                case 264 -> {           // down arrow
                    moveCursorY(1, false);
                    yield true;
                }
                case 265 -> {           // up arrow
                    moveCursorY(-1, false);
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
        StringBuilder result = new StringBuilder();

        int startX;
        int startY;
        int endX;
        int endY;
        if (cursorY == selectionEndY) {
            startY = cursorY;
            endY = cursorY;
            startX = Math.min(cursorX, selectionEndX);
            endX = Math.max(cursorX, selectionEndX);
        }
        else if (cursorY > selectionEndY) {
            startY = selectionEndY;
            endY = cursorY;
            startX = selectionEndX;
            endX = cursorX;
        } else {
            startY = cursorY;
            endY = selectionEndY;
            startX = cursorX;
            endX = selectionEndX;
        }

        for (int i = startY; i <= endY; i++) {
            String line = lines.get(i).getContent();
            if (i == startY && i == endY) {
                result.append(line, startX, startX);
            } else if (i == startY) {
                result.append(line.substring(startX));
            } else if (i == endY) {
                result.append(line, 0, endX);
            } else if (i > startY) {
                result.append(line);
            }
            if (i < lines.size() - 1) {
                result.append("\n");
            }
        }

        return result.toString();
    }

    private void moveCursorX(int i, boolean ignoreSelection) {
        // update selection if selecting
        if (selecting && !ignoreSelection) {
            // start selection if nothing is selected --> start of selection
            if (!hasSelected) {
                selectionEndX = cursorX;
                selectionEndY = cursorY;
                hasSelected = true;
            }
        } else {
            // move cursor to end of selection if something is selected --> end of selection
            if (hasSelected) {
                if (i < 0) {
                    cursorX = Math.min(cursorX, selectionEndX);
                    cursorY = Math.min(cursorY, selectionEndY);
                } else {
                    cursorX = Math.max(cursorX, selectionEndX);
                    cursorY = Math.max(cursorY, selectionEndY);
                }
                hasSelected = false;
                return;
            }
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

    private void moveCursorY(int i, boolean ignoreSelection) {
        // update selection if selecting
        if (selecting && !ignoreSelection) {
            // start selection if nothing is selected --> start of selection
            if (!hasSelected) {
                selectionEndX = cursorX;
                selectionEndY = cursorY;
                hasSelected = true;
            }
        } else {
            // move cursor to end of selection if something is selected --> end of selection
            if (hasSelected) {
                hasSelected = false;
                return;
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
        if (hasSelected) {
            deleteSelection();
        }

        String[] splitString = text.split("\n", -1);

        String afterInsertContent = "";
        if (cursorX < lines.get(cursorY).getContent().length()) {
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
            moveCursorX(splitString[i].length(), true);

        }
        lines.get(cursorY + splitString.length - 1).insert(afterInsertContent, cursorX);
        moveCursorY(splitString.length - 1, true);
        updateWindow();
    }

    private void deleteSelection() {
        int startX;
        int startY;
        int endX;
        int endY;
        if (cursorY == selectionEndY) {
            startY = cursorY;
            endY = cursorY;
            startX = Math.min(cursorX, selectionEndX);
            endX = Math.max(cursorX, selectionEndX);
        }
        else if (cursorY > selectionEndY) {
            startY = selectionEndY;
            endY = cursorY;
            startX = selectionEndX;
            endX = cursorX;
        } else {
            startY = cursorY;
            endY = selectionEndY;
            startX = cursorX;
            endX = selectionEndX;
        }

        List<Integer> linesToDelete = new ArrayList<>();
        int firstLineIndex = -1;
        for (int i = 0; i < lines.size(); i++) {
            // single line selection
            if (i == startY && i == endY) {
                // set cursor so  that the first (only) line is the current line
                cursorX = startX;
                cursorY = startY;
                lines.get(i).cut(startX, endX);
            }
            // start of selection --> cut off text before start
            else if (i == startY) {
                lines.get(i).cut(startX, lines.get(i).getContent().length());
                firstLineIndex = i;
                // set cursor so  that the first line is the current line
                cursorX = lines.get(i).getContent().length();
                cursorY = startY;

            }
            // end of selection --> cut off text after end
            else if (i == endY) {
                lines.get(i).cut(0, endX);
                if (firstLineIndex != -1) {
                    lines.get(firstLineIndex).setContent(lines.get(firstLineIndex).getContent() + lines.get(i).getContent());
                    linesToDelete.add(i);
                }
            }
            // middle of selection --> select whole line
            else if (i > startY && i < endY) {
                linesToDelete.add(i);
            }
        }
        // remove lines that are not needed anymore
        for (int i = linesToDelete.size() - 1; i >= 0; i--) {
            lines.remove((int) linesToDelete.get(i));
        }
        hasSelected = false;
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
            moveCursorY(-1, false);
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX < x || mouseX > x + width || mouseY < y || mouseY > y + height) {
            return Element.super.mouseClicked(mouseX, mouseY, button);
        }
        if (hasSelected) {
            hasSelected = false;
        }

        // Calculate the line number based on the mouse's Y position
        int line = (int) ((mouseY - this.y) / (LINE_HEIGHT * TEXT_SCALE))
            + (scrollPosition == 0 ? 0 : scrollPosition - LINE_HEIGHT) / LINE_HEIGHT; // compensate for last line when scrollposition is more than 0

        // Clamp the line number to the valid range
        line = MathHelper.clamp(line, 0, lines.size() - 1);

        // Get the content of the line
        String content = lines.get(line).getContent();

        // Calculate the column number based on the mouse's X position
        int column = 0;
        for (int i = 0; i < content.length(); i++) {
            if (textRenderer.getWidth(content.substring(windowStartX, windowStartX + i)) > mouseX - this.x) {
                break;
            }
            column = windowStartX + i + 1;
        }

        // Set the cursor's position
        cursorX = column;
        cursorY = line;

        // Update the window to ensure the cursor is visible
        updateWindow();

        return Element.super.mouseClicked(mouseX, mouseY, button);
    }

}
