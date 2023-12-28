package edu.kit.riscjblockits.view.client.screens.handled;

import edu.kit.riscjblockits.view.main.blocks.systemclock.SystemClockScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class SystemClockScreen extends HandledScreen<SystemClockScreenHandler> {
    public SystemClockScreen(SystemClockScreenHandler handler, PlayerInventory inventory,
                             Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

    }
}
