package edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock;

import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;

import static java.lang.Double.NaN;

/**
 * The {@link ModScreenHandler} for the {@link SystemClockBlock} and a {@link  edu.kit.riscjblockits.view.client.screens.handled.SystemClockScreen}.
 */
public class SystemClockScreenHandler extends ModScreenHandler {

    /**
     * This translates the different speeds into ticks per second.
     */
    public static final double[][] SECONDS_TRANSLATIONS = {{0,0}, {80,0.25}, {40,0.5}, {20,1}, {15,1.5}, {10,2}, {5,4}, {2,10}, {1,20}, {9,NaN}}; //assuming 20 ticks per second

    /**
     * Creates a new SystemClockScreenHandler.
     * @param syncId the sync id.
     * @param playerInventory the player inventory.
     * @param blockEntity the block entity that opened the screen.
     */
    public SystemClockScreenHandler(int syncId, PlayerInventory playerInventory, ModBlockEntity blockEntity) {
        super(RISCJ_blockits.SYSTEM_CLOCK_SCREEN_HANDLER, syncId, blockEntity);
        addPlayerInventorySlots(playerInventory);
    }

    /**
     * Creates a new SystemClockScreenHandler.
     * Calls the other constructor with the block entity at the position inside the buf.
     * @param syncId the sync id.
     * @param playerInventory the player inventory.
     * @param buf the packet byte buffer.
     */
    public SystemClockScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, (ModBlockEntity) playerInventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }

}
