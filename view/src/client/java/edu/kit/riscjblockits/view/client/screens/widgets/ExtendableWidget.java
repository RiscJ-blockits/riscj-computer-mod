package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

public abstract class ExtendableWidget implements Drawable, Element, Selectable {

    protected Identifier texture;
    protected int parentWidth;
    protected int parentHeight;
    protected ScreenHandler handler;
    protected boolean open;
    protected boolean narrow;
    protected int leftOffset;



    public void initialize(int parentWidth, int parentHeight, boolean narrow, Identifier texture) {
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        this.narrow = narrow;
        this.leftOffset = this.narrow ? 0 : 86;
        this.texture = texture;
    }

    public void toggleOpen() {
        this.setOpen(!this.open);
    }

    protected void setOpen(boolean opened) {
        if (opened) {
            this.reset();
        }
        this.open = opened;
    }

    public boolean isOpen() {
        return this.open;
    }

    public int findLeftEdge(int width, int backgroundWidth) {
        int i = this.open && !this.narrow ? 177 + (width - backgroundWidth - 200) / 2 : (width - backgroundWidth) / 2;
        return i;
    }

    protected void reset() {
        this.leftOffset = this.narrow ? 0 : 86;
        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight - 166) / 2 ;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!this.isOpen()) {
            return;
        }
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
