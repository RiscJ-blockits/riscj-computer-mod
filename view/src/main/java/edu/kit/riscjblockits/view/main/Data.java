package edu.kit.riscjblockits.view.main;

import edu.kit.riscjblockits.controller.data.IDataContainer;
import net.minecraft.nbt.NbtCompound;

/**
 * This class represents the data container.
 */
public class Data implements IDataContainer {
    /**
     * The compound of the data container.
     */
    private NbtCompound compound;

    /**
     * creates a new data container.
     * @param compound The compound of the new data container.
     */
    public Data(NbtCompound compound) {
        this.compound = compound;
    }
}
