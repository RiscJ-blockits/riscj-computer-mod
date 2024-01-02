package edu.kit.riscjblockits.view.main.data;

import edu.kit.riscjblockits.controller.data.IDataElement;
import net.minecraft.nbt.NbtElement;

public interface IDataNBTConvertable extends IDataElement {
    NbtElement toNbt();
}
