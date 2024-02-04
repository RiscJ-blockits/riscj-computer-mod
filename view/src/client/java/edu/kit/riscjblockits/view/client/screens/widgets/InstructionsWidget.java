package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import net.minecraft.util.Identifier;

public class InstructionsWidget extends ExtendableWidget{

    private static final Identifier TEXTURE = new Identifier(RISCJ_blockits.MODID,"textures/gui/register/reg_select_widget.png");
    private static final String TO_DO_TEXT = "Available Instructions";

    public InstructionsWidget() {

    }

    public void initialize(int parentWidth, int parentHeight, boolean narrow) {
        super.initialize(parentWidth, parentHeight, narrow, TEXTURE);

    }

}
