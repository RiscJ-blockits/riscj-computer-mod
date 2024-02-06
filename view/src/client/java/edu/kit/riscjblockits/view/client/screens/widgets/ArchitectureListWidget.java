package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.view.client.screens.handled.ControlUnitScreen;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitScreenHandler;
import java.util.List;

public class ArchitectureListWidget extends ScrollableListWidget<ArchitectureEntry>{


    public ArchitectureListWidget(List<ArchitectureEntry> entries, int x, int y, int width, int height,
                                  int scrollBarOffset) {
        super(entries, x, y, width, height, scrollBarOffset);
    }

    public ArchitectureListWidget(ControlUnitScreen screen, int x, int y, int width, int height, int scrollBarOffset) {
        this(screen.fetchEntries(), x, y, width, height, scrollBarOffset);
    }

    public void updateEntries(List<ArchitectureEntry> updatedEntries) {
        if(updatedEntries.isEmpty()) {
            this.entries.clear();
            return;
        }
        this.entries = updatedEntries;
    }

}
