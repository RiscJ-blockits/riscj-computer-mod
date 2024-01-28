package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.function.Consumer;


public class ScrollableListWidget<T extends ListEntry> implements Widget, Drawable, Element{

    public static final Identifier
        SCROLLBAR = new Identifier(RISCJ_blockits.MODID, "textures/gui/general/scroller.png");
    public static final Identifier SCROLLBAR_DISABLED = new Identifier(RISCJ_blockits.MODID, "textures/gui/general/scroller_disabled.png");

    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 15;
    private static final int SCROLL_MULTIPLIER = 4;
    protected List<T> entries;
    private int x;
    private int y;
    private int width;
    private int height;

    private final int scrollBarOffset;
    private final int scrollTopY;

    private final int entryPadding = 2;

    private int scrollPosition;
    private boolean scrolling;
    private boolean isFocused;

    public ScrollableListWidget(List<T> entries, int x, int y, int width, int height, int scrollBarOffset) {
        this.entries = entries;
        this.width = width;
        this.height = height;
        this.scrollBarOffset = scrollBarOffset;
        this.scrollTopY = y;
        this.x = x;
        this.y = y;

        this.scrollPosition = 0;
        this.scrolling = false;
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
                entry.render(context, mouseX, mouseY, delta);
            }
            // increment currentY by height of widget + padding
            currentY += entry.getHeight() + entryPadding;
        }


        context.disableScissor();

        drawScrollbar(context);

    }

    private int getContentsHeight() {
        int totalHeight = 0;
        for (ListEntry entry : entries) {
            totalHeight += entry.getHeight() + entryPadding;
        }
        // remove padding from last entry
        return totalHeight - entryPadding;
    }

    private void drawScrollbar(DrawContext context) {
        if (overflows()) {
            context.drawTexture(SCROLLBAR, this.x + this.width + this.scrollBarOffset, y + getScrollbarPosition(), 0, 0, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT);
        }else {
            context.drawTexture(SCROLLBAR_DISABLED, this.x + this.width + this.scrollBarOffset, y, 0, 0, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT);
        }
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return Element.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return Element.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(button == 0){
            scrollPosition += (int) deltaY;
        }

        return Element.super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
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
        isFocused = focused;
    }

    @Override
    public boolean isFocused() {
        return isFocused;
    }

}
