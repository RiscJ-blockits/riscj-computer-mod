package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

/**
 * A scrollable list widget. Can be used to build a list of entries that can be scrolled through.
 * @param <T> The type of the list entries.
 */
public class ScrollableListWidget<T extends ListEntry> implements Widget, Drawable, ParentElement, Selectable {

    private static final Identifier SCROLLER_TEXTURE = new Identifier("container/creative_inventory/scroller");
    private static final Identifier SCROLLER_DISABLED_TEXTURE = new Identifier("container/creative_inventory/scroller_disabled");
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 15;
    private static final int SCROLL_MULTIPLIER = 4;
    private static final int ENTRY_PADDING = 2;
    /**
     * The list of entries.
     */
    protected List<T> entries;
    private int x;
    private int y;
    private final int width;
    private final int height;

    private final int scrollBarOffset;

    private int scrollPosition;
    private boolean isFocused;

    /**
     * Creates a new scrollable list widget.
     * @param entries The list of entries.
     * @param x The x position of the widget.
     * @param y The y position of the widget.
     * @param width The width of the widget.
     * @param height The height of the widget.
     * @param scrollBarOffset The offset of the scrollbar.
     */
    public ScrollableListWidget(List<T> entries, int x, int y, int width, int height, int scrollBarOffset) {
        this.entries = entries;
        this.width = width;
        this.height = height;
        this.scrollBarOffset = scrollBarOffset;
        this.x = x;
        this.y = y;

        this.scrollPosition = 0;
    }

    private boolean overflows() {
        return getContentsHeight() > height;
    }

    private int getScrollbarPosition() {
        return MathHelper.clamp(((scrollPosition * height) / (getContentsHeight() - this.height) ), 0, this.height - SCROLLBAR_HEIGHT);
    }

    @Override
    public void setX(int x) {
        this.x = x;
        for (ListEntry entry: entries) {
            entry.setX(x);
        }
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
        //do nothing
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        context.enableScissor(x, y, x + width, y + height);

        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            isFocused = true;
        }

        int currentY = 0;
        for (ListEntry entry : entries) {
            // only draw an entry when it is between top and bottom of scroll widget
            if (currentY + entry.getHeight() > scrollPosition && currentY < scrollPosition + height) {
                entry.setY(getY() + currentY - scrollPosition);
                entry.setX(getX());
                entry.render(context, mouseX, mouseY, delta);
            }
            // increment currentY by height of widget + padding
            currentY += entry.getHeight() + ENTRY_PADDING;
        }

        context.disableScissor();

        drawScrollbar(context);
    }

    /**
     * Gets the height of all entries combined.
     * @return the height.
     */
    protected int getContentsHeight() {
        int totalHeight = 0;
        for (ListEntry entry : entries) {
            totalHeight += entry.getHeight() + ENTRY_PADDING;
        }
        // remove padding from last entry
        return totalHeight - ENTRY_PADDING;
    }

    /**
     * Draws the scrollbar. It sits on the left side of the widget.
     * @param context The context to draw in.
     */
    protected void drawScrollbar(DrawContext context) {
        if (overflows()) {
            context.drawGuiTexture(SCROLLER_TEXTURE, this.x + this.width + this.scrollBarOffset, this.y + getScrollbarPosition(), SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT);
        } else {
            context.drawGuiTexture(SCROLLER_DISABLED_TEXTURE, this.x + this.width + this.scrollBarOffset, this.y, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT);
        }
    }

    @Override
    public List<? extends Element> children() {
        return entries;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollPosition -= (int) (SCROLL_MULTIPLIER * verticalAmount);
        if (scrollPosition < 0) {
            scrollPosition = 0;
        } else if (scrollPosition > getContentsHeight() - height){
            scrollPosition = getContentsHeight() - height;
        }
        return ParentElement.super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean isDragging() {
        return false;
    }

    @Override
    public void setDragging(boolean dragging) {
        //do nothing
    }

    @Nullable
    @Override
    public Element getFocused() {
        return null;
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        //do nothing
    }

    @Override
    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    @Override
    public boolean isFocused() {
        return isFocused;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        //do nothing
    }

    @Override
    public SelectionType getType() {
        return SelectionType.HOVERED;
    }
}
