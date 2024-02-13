package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;

/**
 * A simple button with an icon texture on top.
 */
public class IconButtonWidget extends ButtonWidget {

    /**
     * For narration support.
     */
    protected static final NarrationSupplier DEFAULT_NARRATION_SUPPLIER = textSupplier -> (MutableText)textSupplier.get();

    /**
     * The texture on top of the button.
     */
    private final Identifier texture;

    /**
     * Constructor for the icon button widget.
     * It creates a button with an icon texture on top.
     * @param x The x position of the button.
     * @param y The y position of the button.
     * @param width The width of the button.
     * @param height The height of the button.
     * @param onPress The action to perform when the button is pressed.
     * @param texture The texture of the button.
     */
    public IconButtonWidget(int x, int y, int width, int height, PressAction onPress, Identifier texture) {
        super(x, y, width, height, null, onPress, DEFAULT_NARRATION_SUPPLIER);
        this.texture = texture;
    }

    /**
     * Draws the button.
     * @param context The context to draw in.
     * @param mouseX The x position of the mouse.
     * @param mouseY The y position of the mouse.
     * @param delta Not specified in the documentation.
     */
    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(texture, getX(), getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
    }

}
