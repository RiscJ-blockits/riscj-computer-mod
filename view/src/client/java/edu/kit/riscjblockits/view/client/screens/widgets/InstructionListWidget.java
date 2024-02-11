package edu.kit.riscjblockits.view.client.screens.widgets;

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

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        for (InstructionEntry entry : entries) {
            entry.drawTooltip(context, mouseX, mouseY, delta);
        }
    }
}
