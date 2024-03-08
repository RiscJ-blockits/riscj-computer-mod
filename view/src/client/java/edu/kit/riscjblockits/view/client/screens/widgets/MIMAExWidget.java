package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.util.Identifier;

/**
 * A side widget that displays an example Mima build.
 */
public class MIMAExWidget extends ExtendableWidget {

    /**
     * The button textures for the expand button that opens the widget.
     */
    public static final ButtonTextures BUTTON_TEXTURES = new ButtonTextures(new Identifier("recipe_book/button"), new Identifier("recipe_book/button_highlighted"));

    private static final Identifier
        TEXTURE = new Identifier(RISCJ_blockits.MOD_ID, "textures/gui/control_unit/control_mima_ex.png");

    /**
     * Creates a new MimaExWidget.
     */
    public MIMAExWidget() {
        //the initialize() method acts like a constructor in a lot of the widgets
    }

    /**
     * Initializes the widget. Adds the picture of the Mima computer to the widget.
     * @param parentWidth The width of the parent widget.
     * @param parentHeight The height of the parent widget.
     * @param narrow Only used by minecraft.
     */
    public void initialize(int parentWidth, int parentHeight, boolean narrow) {
        super.initialize(parentWidth, parentHeight, narrow, TEXTURE);
        this.open = false;
    }
}
