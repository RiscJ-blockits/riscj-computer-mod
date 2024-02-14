package edu.kit.riscjblockits.view.main.blocks.mod.computer.systemclock;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.ModScreenHandler;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import static edu.kit.riscjblockits.model.blocks.ClockMode.STEP;
import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_MODE;
import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_SPEED;
import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;

/**
 * The {@link ModScreenHandler} for the {@link SystemClockBlock} and a {@link  edu.kit.riscjblockits.view.client.screens.handled.SystemClockScreen }.
 */
public class SystemClockScreenHandler extends ModScreenHandler {

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

    /**
     * Getter for the system clock speed.
     * @return the speed of the system clock from the model.
     */
    public int getSystemClockSpeed() {
        NbtCompound nbt = getBlockEntity().createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return 0;
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return 0;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(CLOCK_SPEED)) {
                return Integer.parseInt(((IDataStringEntry) ((IDataContainer) data).get(s)).getContent());
            }
        }
        return 0;
    }

    /**
     * Getter for the system clock mode.
     * @return the mode of the system clock from the model.
     */
    public String getSystemClockMode() {
        NbtCompound nbt = getBlockEntity().createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return STEP.toString();
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return STEP.toString();
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(CLOCK_MODE)) {
                //ToDo maybe refactor this to return the enum
                return ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
            }
        }
        return STEP.toString();
    }

}
