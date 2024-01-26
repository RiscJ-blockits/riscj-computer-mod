package edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;

public class SystemClockScreenHandler extends ModScreenHandler {

    public SystemClockScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(RISCJ_blockits.SYSTEM_CLOCK_SCREEN_HANDLER, syncId);

        addPlayerInventorySlots(playerInventory);
    }

    public SystemClockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory);
    }

}
