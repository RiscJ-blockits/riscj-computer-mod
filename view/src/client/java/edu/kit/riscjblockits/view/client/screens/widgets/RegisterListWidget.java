package edu.kit.riscjblockits.view.client.screens.widgets;

import java.util.List;

public class RegisterListWidget extends ScrollableListWidget<RegisterEntry> {
    private static final int SCROLLBAR_OFFSET_X = 6;

    public RegisterListWidget(List<RegisterEntry> entries, int x, int y, int width, int height) {
        super(entries, x, y, width, height, SCROLLBAR_OFFSET_X);

        for (RegisterEntry entry: entries) {
            entry.setX(x);
        }
    }
}
