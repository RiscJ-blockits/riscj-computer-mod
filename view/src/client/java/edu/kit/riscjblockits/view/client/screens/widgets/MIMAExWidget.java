package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.util.Identifier;


public class MIMAExWidget extends ExtendableWidget {

    public static final ButtonTextures BUTTON_TEXTURES = new ButtonTextures(new Identifier("recipe_book/button"), new Identifier("recipe_book/button_highlighted"));
    public static final Identifier
        TEXTURE = new Identifier(RISCJ_blockits.MODID, "textures/gui/control_unit/control_mima_ex.png");

    public MIMAExWidget() {
    }

    public void initialize(int parentWidth, int parentHeight, boolean narrow) {
        super.initialize(parentWidth, parentHeight, narrow, TEXTURE);
    }
}
