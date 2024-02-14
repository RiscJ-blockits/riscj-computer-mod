package edu.kit.riscjblockits.view.client.screens.widgets;

import java.util.List;

/**
 * A RegisterListWidget is a widget that displays a list of registers.
 * In it, you can select a registertype.
 */
public class RegisterListWidget extends ScrollableListWidget<RegisterEntry> {
    private static final int SCROLLBAR_OFFSET_X = 6;

    /**
     * Creates a new RegisterListWidget.
     * @param entries The entries to display.
     * @param x The x position of the widget.
     * @param y The y position of the widget.
     * @param width The width of the widget.
     * @param height The height of the widget.
     */
    public RegisterListWidget(List<RegisterEntry> entries, int x, int y, int width, int height) {
        super(entries, x, y, width, height, SCROLLBAR_OFFSET_X);
        for (RegisterEntry entry: entries) {
            entry.setX(x);
        }
    }

    /**
     * Updates the entries of the list with selectable register types.
     * @param updatedEntries The updated entries.
     */
    public void updateEntries(List<RegisterEntry> updatedEntries) {
        if(updatedEntries.size() > this.entries.size()) {
            this.entries = updatedEntries;
            return;
        }
        for (RegisterEntry entry: this.entries) {
            for (RegisterEntry updatedEntry : updatedEntries) {
                if (entry.getName().equals(updatedEntry.getName())) {
                    entry.update(updatedEntry.isMissing(), updatedEntry.isCurrentReg());
                }
            }
        }
    }
}
