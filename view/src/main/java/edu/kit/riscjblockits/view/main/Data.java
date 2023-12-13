package edu.kit.riscjblockits.view.main;

import edu.kit.riscjblockits.controller.data.IDataContainer;
import net.minecraft.nbt.NbtCompound;

public class Data implements IDataContainer {
    private NbtCompound compound;

    public Data(NbtCompound compound) {
        this.compound = compound;
    }
}
