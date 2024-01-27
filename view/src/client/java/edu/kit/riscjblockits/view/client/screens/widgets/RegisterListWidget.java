package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.model.data.DataConstants;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;

import java.util.List;

public class RegisterListWidget extends ScrollableListWidget<RegisterEntry> implements Drawable, Element {
    private static final int SCROLLBAR_OFFSET_X = 6;

    public RegisterListWidget(List<RegisterEntry> entries, int x, int y, int width, int height) {
        super(entries, x, y, width, height, SCROLLBAR_OFFSET_X);

        for (RegisterEntry entry: entries) {
            entry.setX(x);
        }
    }
}
