package edu.kit.riscjblockits.view.main.blocks.mod.computer.register.io;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.view.main.RISCJ_blockits;
import edu.kit.riscjblockits.view.main.blocks.mod.ModBlockEntity;
import edu.kit.riscjblockits.view.main.blocks.mod.computer.register.RegisterScreenHandler;
import edu.kit.riscjblockits.view.main.data.NbtDataConverter;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import static edu.kit.riscjblockits.model.data.DataConstants.MOD_DATA;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_IN_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_MODE_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TERMINAL_OUT_TYPE;

/**
 * Handles an {@link edu.kit.riscjblockits.view.client.screens.handled.TerminalScreen}.
 */
public class TerminalScreenHandler extends RegisterScreenHandler {

    /**
     * Constructor for the terminal screen handler.
     * @param syncId The sync id.
     * @param inventory The player inventory.
     * @param blockEntity The block entity.
     */
    public TerminalScreenHandler(int syncId, PlayerInventory inventory, ModBlockEntity blockEntity) {
        super(RISCJ_blockits.TERMINAL_SCREEN_HANDLER, syncId, blockEntity);
        addPlayerInventorySlotsLarge(inventory);
    }

    /**
     * Constructor for the terminal screen handler.
     * @param syncId The sync id.
     * @param inventory The player inventory.
     * @param buf The packet byte buffer with the location of the block entity.
     */
    public TerminalScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, (ModBlockEntity) inventory.player.getWorld().getBlockEntity(buf.readBlockPos()));
    }

    /**
     * Returns the current register of the terminal depending on which of the three registers is selected.
     * @param mode The mode of the terminal screen.
     * @return The current register type.
     */
    public String getCurrentRegister(DisplayMode mode) {
        NbtCompound nbt = getBlockEntity().createNbt();
        if (!nbt.contains(MOD_DATA)) {
            return "";
        }
        IDataElement data = new NbtDataConverter(nbt.get(MOD_DATA)).getData();
        if (!data.isContainer()) {
            return "";
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(REGISTER_TERMINAL_MODE_TYPE) && (mode == DisplayMode.MODE)) {
                return ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
            } else if (s.equals(REGISTER_TERMINAL_IN_TYPE) && (mode == DisplayMode.IN)) {
                return ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
            } else if (s.equals(REGISTER_TERMINAL_OUT_TYPE) && (mode == DisplayMode.OUT)) {
                return ((IDataStringEntry) ((IDataContainer) data).get(s)).getContent();
            }
        }
        return "";
    }

    /**
     * The display mode of the terminal screen.
     * It decides which of the three registers should be changed
     */
    public enum DisplayMode {
        /**
         * The input register. Used for the input of the player.
         */
        IN,
        /**
         * The output register. Used for the output to the terminal screen.
         */
        OUT,
        /**
         * The mode register. Used to put the next input into the input register or to reset the input register.
         */
        MODE
    }

}
