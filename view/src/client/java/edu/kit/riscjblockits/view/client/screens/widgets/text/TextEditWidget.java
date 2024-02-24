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

/**
 * A widget that allows the user to edit text.
 * Allows for selecting, copying, cutting, and pasting text.
 */
public class TextEditWidget implements Widget, Drawable, Element, Selectable {
    /**
     * The height of a line.
     */
    protected static final int LINE_HEIGHT = 9;
    /**
     * The color of the text.
     */
    protected static final int TEXT_COLOR = 0xa9b7c6;
    /**
     * The color of the selection.
     */
    protected static final int SELECTION_COLOR = 0x99214283;
    /**
     * The multiplier for scrolling.
     */
    private static final int SCROLL_MULTIPLIER = 6;
    /**
     * The scale of the text.
     */
    private static final float TEXT_SCALE = 0.66f;
    /**
     * The inverse of the text scale.
     */
    private static final float INVERSE_TEXT_SCALE = 1 / TEXT_SCALE;
    /**
     * The number of spaces in a tab.
     */
    private static final int TAB_SPACE_COUNT = 4;
    /**
     * The offset between editor and line index.
     */
    private static final int LINE_INDEX_OFFSET = 6;
    /**
     * The expected width of the line index.
     */
    private static final int LINE_INDEX_EXPECTED_WIDTH = 18;
    private static final int MAX_LINES_AMOUNT = 1000000;
    /**
     * the textRenderer used to render the text.
     */
    protected final TextRenderer textRenderer;
    /**
     * The lines of text.
     */
    private List<Line> lines;
    /**
     * The position of the user is scrolled to.
     */
    private int scrollPosition = 0;
    /**
     * The x position of the widget.
     */
    private int x;
    /**
     * The y position of the widget.
     */
    private int y;
    /**
     * The width of the widget.
     */
    private final int width;
    /**
     * The height of the widget.
     */
    private final int height;
    /**
     * the time of the widget (used for cursor blinking).
     */
    private float tickCounter = 0;
    /**
     * The x position of the cursor.
     */
    private int cursorX = 0;
    /**
     * The y position of the cursor.
     */
    private int cursorY = 0;
    /**
     * The x position of the viewing window.
     */
    private int windowStartX = 0;
    /**
     * Whether the user is currently selecting.
     */
    private boolean selecting = false;
    /**
     * The x position of the selection end.
     */
    private int selectionEndX = 0;
    /**
     * The y position of the selection end.
     */
    private int selectionEndY = 0;
    /**
     * Whether something is selected.
     */
    private boolean hasSelected = false;

    /**
     * Creates a new TextEditWidget.
     * @param textRenderer the textRenderer used to render the text
     * @param x the x position of the widget
     * @param y the y position of the widget
     * @param width the width of the widget
     * @param height the height of the widget
     */
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
        context.enableScissor(x - LINE_INDEX_OFFSET - LINE_INDEX_EXPECTED_WIDTH, y, x + width, y + height);
        // prepare text scaling
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.scale(TEXT_SCALE, TEXT_SCALE, TEXT_SCALE);
        // draw lines with their index
        for (int i = start; i < lines.size(); i++) {
            if ((i - start) * LINE_HEIGHT >= height * INVERSE_TEXT_SCALE) {
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
        }
        // draw cursor, different mode if cursor is at the end of the line
        if (lines.get(cursorY).getContent().length() <= cursorX){
            drawCursor(context,
                    (int) (x * INVERSE_TEXT_SCALE) + textRenderer.getWidth(lines.get(cursorY).getContentUntil(cursorX).substring(windowStartX)),
                    (int) (y * INVERSE_TEXT_SCALE + (cursorY - start) * LINE_HEIGHT), true);
        } else
            drawCursor(context,
                    (int) (x * INVERSE_TEXT_SCALE) + textRenderer.getWidth(lines.get(cursorY).getContentUntil(cursorX).substring(windowStartX)),
                    (int) (y * INVERSE_TEXT_SCALE + (cursorY - start) * LINE_HEIGHT), false);

        matrixStack.pop();
        context.disableScissor();

    }

    /**
     * Draws the given line.
     * @param context the context to draw in
     * @param line the line to draw
     * @param windowStartX the x position of the window
     * @param i the index of the line
     * @param displayX the x position to draw the line
     * @param displayY the y position to draw the line
     */
    protected void drawLine(DrawContext context, String line, int windowStartX, int i, int displayX, int displayY) {
        if (line.length() < windowStartX) {
            return;
        }
        context.drawText(textRenderer, line.substring(windowStartX), displayX, displayY, TEXT_COLOR, false);
    }

    /**
     * Draws the selection for the given line.
     * @param context the context to draw in
     * @param i the index of the line
     * @param displayY the y position to draw the selection
     */
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
        } else if (cursorY > selectionEndY) {
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

    /**
     * Returns the selected text for the given line.
      * @param i the index of the line
     * @param startX the x position of the start of the selection
     * @param startY the y position of the start of the selection
     * @param endX the x position of the end of the selection
     * @param endY the y position of the end of the selection
     * @return the selected text for the given line
     */
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

    /**
     * Draws the line index for the given line.
     * @param context the context to draw in
     * @param i the index of the line
     * @param y the y position to draw the line index
     */
    private void drawLineIndex(DrawContext context, int i, int y) {
        int width = textRenderer.getWidth(String.valueOf(i));
        context.drawText(textRenderer, String.valueOf(i), (int) (x * INVERSE_TEXT_SCALE - width - LINE_INDEX_OFFSET * INVERSE_TEXT_SCALE), y, TEXT_COLOR, false);
    }

    /**
     * Draws the selection.
     * @param context the context to draw in
     * @param x the x position of the selection
     * @param y the y position of the selection
     * @param width the width of the selection
     */
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

    /**
     * Returns the height of the contents.
     * @return the height of the contents
     */
    private int getContentsHeight() {
        return (int) ((lines.size() + 3) * LINE_HEIGHT * TEXT_SCALE);
    }

    /**
     * Sets the text of the widget.
     * @param text the text to set
     */
    public void setText(String text) {
        this.lines = new ArrayList<>();
        for (String lineString : text.split("\n", -1)) {
            lines.add(new Line(lineString));
        }
        cursorX = 0;
        cursorY = 0;
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

    /**
     * Returns the text of the widget.
     * @return the text of the widget
     */
    public String getText() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            result.append(lines.get(i).getContent());
            if (i < lines.size() - 1)
                result.append("\n");
        }
        return result.toString();
    }

    private void drawCursor(DrawContext context, int x, int y, boolean endOfLine) {
        if (((int) this.tickCounter) / 6 % 2 == 0 && isFocused()) {
            context.drawText(this.textRenderer, endOfLine ? "_" : "|", x, y, endOfLine ? TEXT_COLOR : 0x999999, false);
        }
    }

    /**
     * Updates the window.
     * @param chr the character that was typed
     * @param modifiers the modifiers that were pressed
     * @return whether the character was typed
     */
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

    /**
     * Handles a key press.
     * @param keyCode the key code of the key that was pressed
     * @param scanCode the scan code of the key that was pressed
     * @param modifiers the modifiers that were pressed
     * @return whether the key was pressed
     */
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.selecting = Screen.hasShiftDown();
        if (Screen.isSelectAll(keyCode)) {
            this.selectionEndX = 0;
            this.selectionEndY = 0;
            this.cursorY = lines.size() - 1;
            this.cursorX = lines.get(cursorY).getContent().length();
            hasSelected = true;
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
                        yield true;
                    }
                    if (Screen.hasControlDown()) {
                        delete(-lines.get(cursorY).getPreviousWordLength(cursorX));
                        yield true;
                    }
                    delete(-1);
                    yield true;
                }
                case 261 -> {           // delete
                    if (hasSelected) {
                        insert("");
                        yield true;
                    }
                    if (Screen.hasControlDown()) {
                        delete(lines.get(cursorY).getNextWordLength(cursorX));
                        yield true;
                    }
                    delete(1);
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
        } else if (cursorY > selectionEndY) {
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
                result.append(line, startX, endX);
            } else if (i == startY) {
                result.append(line.substring(startX));
            } else if (i == endY) {
                result.append(line, 0, endX);
            } else if (i > startY) {
                result.append(line);
            }
            if (i < lines.size() - 1 && i != endY) {
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

        // add missing lines
        for (int k = cursorY + 1; k < Math.min(cursorY + splitString.length, MAX_LINES_AMOUNT); k++)
            lines.add(k, new Line());

        for (int i = 0; i < splitString.length; i++) {
            int j = i + cursorY;
            if (j >= MAX_LINES_AMOUNT - 1) {
                cursorX = lines.get(lines.size() - 1).getContent().length();
                lines.get(lines.size() - 1).insert(afterInsertContent, cursorX);
                cursorY = lines.size() - 1;
                return;
            }

            if ( i >= 1) {
                cursorX = 0;
            }
            Line line = lines.get(j);
            line.insert(splitString[i], cursorX);
        }
        cursorX = splitString[splitString.length - 1].length();
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

    /**
     * Deletes the given amount of characters.
     * @param i the amount of characters to delete
     */
    public void delete(int i) {

        if (i == 0)
            return;

        int columnDelta = 0;
        int from;
        int to;
        // backspace
        if (i < 0) {
            if (cursorX == 0) {
                unhookLine(cursorY);
                return;
            }
            from = cursorX + i;
            to = cursorX;
            columnDelta = i;
        }
        // delete
        else {
            if (lines.get(cursorY).getContent().length() == cursorX) {
                unhookLine(cursorY + 1);
                return;
            }
            to = cursorX + i;
            from = cursorX;
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
        if ((cursorY + 1) * LINE_HEIGHT * TEXT_SCALE < scrollPosition) {
            scrollPosition = Math.round((cursorY + 1) * LINE_HEIGHT * TEXT_SCALE);
        }

        // cursor going out the bottom
        if ((cursorY + 2) * LINE_HEIGHT * TEXT_SCALE > scrollPosition + height) {
            scrollPosition = (Math.round((cursorY + 2) * LINE_HEIGHT * TEXT_SCALE) - height);
        }

        // cursor going out of left side
        int totalCharacters = lines.get(cursorY).getContent().length();
        if (cursorX <= windowStartX) {
            windowStartX = MathHelper.clamp(cursorX - 1, 0, totalCharacters);
        }

        // cursor going out of right side
        int shownCharacters = textRenderer.trimToWidth(lines.get(cursorY).getContent().substring(windowStartX), (int) (width * INVERSE_TEXT_SCALE)).length();
        if (cursorX > (windowStartX + shownCharacters))
            windowStartX = (cursorX - shownCharacters);
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
        int line = (int) Math.round((mouseY - this.y + scrollPosition) / (LINE_HEIGHT * TEXT_SCALE)) - (scrollPosition > 0 ? 2 : 0);

        // Clamp the line number to the valid range
        line = MathHelper.clamp(line, 0, lines.size() - 1);

        // Get the content of the line
        String content = lines.get(line).getContent();

        // Calculate the column number based on the mouse's X position
        int column = 0;
        for (int i = 0; i < content.length() - windowStartX; i++) {
            if (textRenderer.getWidth(content.substring(windowStartX, windowStartX + i)) > (mouseX - this.x) * INVERSE_TEXT_SCALE) {
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
