package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterScreenHandler;

import java.util.List;

public class RegisterListWidget extends ScrollableListWidget<RegisterEntry> {
    private static final int SCROLLBAR_OFFSET_X = 6;

    public RegisterListWidget(List<RegisterEntry> entries, int x, int y, int width, int height) {
        super(entries, x, y, width, height, SCROLLBAR_OFFSET_X);

        for (RegisterEntry entry: entries) {
            entry.setX(x);
        }
    }

    public void updateEntries(List<RegisterEntry> updatedEntries) {
        //Fixme: this is not working
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
