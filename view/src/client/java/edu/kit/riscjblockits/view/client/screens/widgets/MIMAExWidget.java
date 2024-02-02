package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class MIMAExWidget implements Drawable, Element, Selectable {

    public static final ButtonTextures BUTTON_TEXTURES = new ButtonTextures(new Identifier("recipe_book/button"), new Identifier("recipe_book/button_highlighted"));
    public static final Identifier
        TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/control_unit/control_mima_ex.png");

    private int parentWidth;
    private int parentHeight;
    private boolean open;
    private boolean narrow;
    private int leftOffset;

    public MIMAExWidget() {
    }

    public void initalize(int parentWidth, int parentHeight, boolean narrow) {
        this.parentWidth = parentWidth;
        this.parentHeight = parentHeight;
        this.narrow = narrow;
        this.leftOffset = this.narrow ? 0 : 86;
    }

    public void toggleOpen() {
        this.setOpen(!this.open);
    }
    private void setOpen(boolean opened) {
        this.open = opened;
    }

    public int findLeftEdge(int width, int backgroundWidth) {
        int i = this.open && !this.narrow ? 177 + (width - backgroundWidth - 200) / 2 : (width - backgroundWidth) / 2;
        return i;
    }

    public boolean isOpen() {
        return this.open;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!this.isOpen()) {
            return;
        }
        context.getMatrices().push();
        context.getMatrices().translate(0.0f, 0.0f, 100.0f);

        int i = (this.parentWidth - 147) / 2 - this.leftOffset;
        int j = (this.parentHeight - 166) / 2;
        context.drawTexture(TEXTURE, i, j, 1, 1, 147, 166);

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
}
