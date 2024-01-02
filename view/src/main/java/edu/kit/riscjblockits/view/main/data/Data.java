package edu.kit.riscjblockits.view.main.data;

import edu.kit.riscjblockits.controller.data.IDataContainer;
import edu.kit.riscjblockits.controller.data.IDataElement;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.HashMap;
import java.util.Map;

public class Data implements IDataContainer, IDataNBTConvertable {

    private final Map<String, IDataElement> contents = new HashMap<>();

    public Data(NbtCompound compound) {
        loadFromNbt(compound);
    }

    public Data () {

    }

    private void loadFromNbt(NbtCompound compound) {
        for (String key : compound.getKeys()) {
            NbtElement nbtElement = compound.get(key);
            if (nbtElement.getType() == NbtType.STRING)
                contents.put(key, new DataStringEntry(nbtElement.asString()));
            else if (nbtElement.getType() == NbtType.COMPOUND)
                contents.put(key, new Data((NbtCompound) nbtElement));
        }
    }

    public void set(String key, IDataElement value) {
        contents.put(key, value);
    }

    @Override
    public IDataElement get(String key) {
        if (contents.containsKey(key))
            return contents.get(key);

        // empty data
        return new Data();
    }

    public NbtElement toNbt() {
        NbtCompound compound = new NbtCompound();
        for (Map.Entry<String, IDataElement> entry : contents.entrySet()) {
            if (entry.getValue() instanceof IDataNBTConvertable)
                compound.put(entry.getKey(), ((IDataNBTConvertable) entry.getValue()).toNbt());
        }
        return compound;
    }
}
