package edu.kit.riscjblockits.view.client.screens.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class InstructionListWidget extends ScrollableListWidget<InstructionEntry>{

    private static final int SCROLLBAR_OFFSET_X = 6;

    public InstructionListWidget(List<InstructionEntry> entries, int x, int y, int width, int height) {
        super(entries, x, y, width, height, SCROLLBAR_OFFSET_X);
    }

    public void updateEntries(List<InstructionEntry> updatedEntries) {
            this.entries = updatedEntries;
    }
}
