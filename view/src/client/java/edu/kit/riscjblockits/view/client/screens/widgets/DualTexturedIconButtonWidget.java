package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.util.Identifier;

/**
 * A IconButton button with two icon textures.
 */
public class DualTexturedIconButtonWidget extends IconButtonWidget{

    /**
     * The first texture of the button.
     */
    private final Identifier texture1;
    /**
     * The second texture of the button.
     */
    private final Identifier texture2;

    /**
     * Constructor for the icon button widget.
     * It creates a button with an icon texture on top.
     *
     * @param x            The x position of the button.
     * @param y            The y position of the button.
     * @param width        The width of the button.
     * @param height       The height of the button.
     * @param onPress      The action to perform when the button is pressed.
     * @param texture1     The default texture of the button.
     */
    public DualTexturedIconButtonWidget(int x, int y, int width, int height, PressAction onPress, Identifier texture1, Identifier texture2) {
        super(x, y, width, height, onPress, texture1);
        this.texture1 = texture1;
        this.texture2 = texture2;
    }

    /**
     * Sets the texture of the button.
     * @param isTexture1 true if the texture should be texture1, false if it should be texture2.
     */
    public void setTexture(boolean isTexture1) {
        if (isTexture1) {
            this.texture = texture1;
        } else {
            this.texture = texture2;
        }
    }

}
