package edu.kit.riscjblockits.view.client.screens.widgets;

import edu.kit.riscjblockits.model.data.DataConstants;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.controlunit.ControlUnitScreenHandler;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ArchitectureListWidget extends ScrollableListWidget<ArchitectureEntry>{


    public ArchitectureListWidget(List<ArchitectureEntry> entries, int x, int y, int width, int height,
                                  int scrollBarOffset) {
        super(entries, x, y, width, height, scrollBarOffset);
    }

    public ArchitectureListWidget(ControlUnitScreenHandler handler, int x, int y, int width, int height, int scrollBarOffset) {
        this(fetchEntries(handler), x, y, width, height, scrollBarOffset);
    }

    private static List<ArchitectureEntry> fetchEntries(ControlUnitScreenHandler controlUnitHandler) {
        BlockPos pos = controlUnitHandler.getBlockEntity().getPos();
        List<ArchitectureEntry> entries = new ArrayList<>();
        for (String component: controlUnitHandler.getArchitecture("missing")) { //TODO fix key, cooperate with Leon
            entries.add(new ArchitectureEntry(component, true));
        }
        for (String component: controlUnitHandler.getArchitecture("found")) {   //TODO fix key, cooperate with Leon
            entries.add(new ArchitectureEntry(component, false));
        }
        return entries;
    }

}
