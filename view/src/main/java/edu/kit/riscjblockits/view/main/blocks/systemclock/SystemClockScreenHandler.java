package edu.kit.riscjblockits.view.main.blocks.systemclock;

import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public class SystemClockScreenHandler extends ModScreenHandler {
    protected SystemClockScreenHandler(@Nullable ScreenHandlerType<?> type,
                                       int syncId) {
        super(type, syncId);
    }

}
