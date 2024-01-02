package edu.kit.riscjblockits.view.client.screens.handled;

import edu.kit.riscjblockits.view.main.blocks.programming.ProgrammingScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class ProgrammingScreen extends HandledScreen<ProgrammingScreenHandler> {
    public ProgrammingScreen(ProgrammingScreenHandler handler, PlayerInventory inventory,
                             Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

    }

}
