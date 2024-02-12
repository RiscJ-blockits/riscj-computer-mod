package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.client.screens.handled.ControlUnitScreen;
import java.util.List;

/**
 * A widget that displays a list of architecture entries.
 */
public class ArchitectureListWidget extends ScrollableListWidget<ArchitectureEntry>{

    /**
     * Creates a new ArchitectureListWidget. It can display a list of architecture entries.
     * @param entries The entries to display.
     * @param x The x position of the widget.
     * @param y The y position of the widget.
     * @param width The width of the widget.
     * @param height The height of the widget.
     * @param scrollBarOffset The offset of the scrollbar.
     */
    public ArchitectureListWidget(List<ArchitectureEntry> entries, int x, int y, int width, int height,
                                  int scrollBarOffset) {
        super(entries, x, y, width, height, scrollBarOffset);
    }

    /**
     * Creates a new ArchitectureListWidget. It can display a list of architecture entries.
     * @param screen The screen that the widget is on.
     * @param x The x position of the widget.
     * @param y The y position of the widget.
     * @param width The width of the widget.
     * @param height The height of the widget.
     * @param scrollBarOffset The offset of the scrollbar.
     */
    public ArchitectureListWidget(ControlUnitScreen screen, int x, int y, int width, int height, int scrollBarOffset) {
        this(screen.fetchEntries(), x, y, width, height, scrollBarOffset);
    }

    /**
     * Replaces the current entries with the given entries.
     * @param updatedEntries The new entries.
     */
    public void updateEntries(List<ArchitectureEntry> updatedEntries) {
        if(updatedEntries.isEmpty()) {
            this.entries.clear();
            return;
        }
        this.entries = updatedEntries;
    }

}
