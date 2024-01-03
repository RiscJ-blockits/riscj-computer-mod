package edu.kit.riscjblockits.view.client.screens.handled;

import edu.kit.riscjblockits.view.main.blocks.programming.ProgrammingScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;


/**
 * This class represents the programming screen.
 * It will be opened when a player uses a programming block.
 * It will be closed when the player closes the screen.
 * It provides a text field to enter the code.
 */
public class ProgrammingScreen extends HandledScreen<ProgrammingScreenHandler> {

    /**
     * The edit box widget that is used to enter the code.
     */
    private EditBoxWidget editBox;

    /**
     * The button that is used to assemble the code.
     */
    private ButtonWidget assembleButton;


    /**
     * Creates a new ProgrammingScreen.
     * @param handler the handler of the screen
     * @param inventory the player inventory
     * @param title the title of the screen
     */
    public ProgrammingScreen(ProgrammingScreenHandler handler, PlayerInventory inventory,
                             Text title) {
        super(handler, inventory, title);
    }


    /**
     * Initializes the screen.
     * Adds the edit box widget to the screen.
     * Adds the button to the screen.
     */
    @Override
    protected void init() {
        super.init();
        editBox = new EditBoxWidget(textRenderer, (this.width - 340) / 2 + 10, 10, 320, 20, Text.of("test1"), Text.of("test2"));
        addDrawableChild(editBox);
    }

    /**
     * Renders the background of the screen.
     * @param context the drawing context
     * @param mouseX the x position of the mouse
     * @param mouseY the y position of the mouse
     * @param delta the time delta since the last frame
     */
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

    }

}
