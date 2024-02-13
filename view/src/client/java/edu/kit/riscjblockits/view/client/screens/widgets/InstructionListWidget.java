package edu.kit.riscjblockits.view.client.screens.widgets;

import java.util.List;

/**
 * A widget that displays a list of instructions.
 * Is scrollable.
 */
public class InstructionListWidget extends ScrollableListWidget<InstructionEntry>{

    private static final int SCROLLBAR_OFFSET_X = 6;

    /**
     * Constructor for the instruction list widget.
     * @param entries The entries inside the list.
     * @param x The x position of the widget.
     * @param y The y position of the widget.
     * @param width The width of the widget.
     * @param height The height of the widget.
     */
    public InstructionListWidget(List<InstructionEntry> entries, int x, int y, int width, int height) {
        super(entries, x, y, width, height, SCROLLBAR_OFFSET_X);
    }

    /**
     * Updates the entries of the list.
     * @param updatedEntries The entries to replace the old ones with.
     */
    public void updateEntries(List<InstructionEntry> updatedEntries) {
            this.entries = updatedEntries;
    }

}
