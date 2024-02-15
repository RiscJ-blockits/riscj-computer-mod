package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

/**
 * A widget that can be drawn right as an extension to an existing screen.
 */
public abstract class ExtendableWidget implements Drawable, Element, Selectable {

    /**
     * The background texture of the widget.
     */
    protected Identifier texture;

    /**
     * Width of the whole screen.
     */
    protected int parentWidth;

    /**
     * Height of the whole screen.
     */
    protected int parentHeight;

    /**
     * The screen handler of the parent screen.
     */
    protected ScreenHandler handler;
    /**
     * Whether the widget is open or not.
     */
    protected boolean open;
    /**
     * Whether the screen is narrow to resize it.
     */
    protected boolean narrow;
    /**
     * The left offset of the widget.
     */
    protected int leftOffset;

    /**
     * Initializes the widget.
     * @param parentWidth The width of the parent screen.
     * @param parentHeight The height of the parent screen.
     * @param narrow Whether this widget is visible or not.
     * @param texture The background texture of the widget.
     */
    public void initialize(int parentWidth, int parentHeight, boolean narrow, Identifier texture) {
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        this.narrow = narrow;
        this.leftOffset = this.narrow ? 0 : 86;
        this.texture = texture;
    }

    /**
     * Toggles the open state of the widget.
     */
    public void toggleOpen() {
        this.setOpen(!this.open);
    }

    /**
     * Sets the open state of the widget.
     * @param opened The new open state of the widget.
     */
    protected void setOpen(boolean opened) {
        if (opened) {
            this.reset();
        }
        this.open = opened;
    }

    /**
     * Returns whether the widget is open or not.
     * @return true if the widget is open, false otherwise.
     */
    public boolean isOpen() {
        return this.open;
    }

    /**
     * Returns the left edge of the widget.
     * @param width The width of the widget.
     * @param backgroundWidth The width of the parent.
     * @return The left edge of the widget.
     */
    public int findLeftEdge(int width, int backgroundWidth) {
        return this.open && !this.narrow ? 177 + (width - backgroundWidth - 200) / 2 : (width - backgroundWidth) / 2;
    }

    /**
     * Resets the left offset of the widget.
     */
    protected void reset() {
        this.leftOffset = this.narrow ? 0 : 86;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!this.isOpen()) return;
        context.getMatrices().push();
        context.getMatrices().translate(0.0f, 0.0f, 100.0f);
        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight) / 2;
        context.drawTexture(texture, i, j, 1, 1, 147, 166);
        context.getMatrices().pop();
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public SelectionType getType() {
        return this.open ? SelectionType.HOVERED : SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return true;
    }
}
