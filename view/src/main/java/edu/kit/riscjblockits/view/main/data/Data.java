package edu.kit.riscjblockits.view.main.data;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a data container that can be converted to and from NBT.
 * It is used to convert Minecraft's NBT data to our own data format.
 */
public class Data implements IDataContainer, IDataNBTConvertable {

    /**
     * The contents of this data container.
     * The key Map's key is the key the data is stored under.
     * The value is the {@link IDataElement} itself.
     */
    private final Map<String, IDataElement> contents = new HashMap<>();

    /**
     * Creates a new Data container from a NBT compound.
     *
     * @param compound The NBT compound to load from.
     */
    public Data(NbtCompound compound) {
        loadFromNbt(compound);
    }

    /**
     * Creates a new empty Data container.
     */
    public Data () {

    }

    /**
     * Recursively loads the data from a NBT compound.
     *
     * @param compound The NBT compound to load from.
     */
    private void loadFromNbt(NbtCompound compound) {
        for (String key : compound.getKeys()) {
            NbtElement nbtElement = compound.get(key);
            if (nbtElement.getType() == NbtType.STRING)
                contents.put(key, new DataStringEntry(nbtElement.asString()));
            else if (nbtElement.getType() == NbtType.COMPOUND)
                contents.put(key, new Data((NbtCompound) nbtElement));
        }
    }

    /**
     * Converts this data container to a NBT compound.
     *
     * @return The NBT compound.
     */
    public NbtElement toNbt() {
        NbtCompound compound = new NbtCompound();
        for (Map.Entry<String, IDataElement> entry : contents.entrySet()) {
            if (entry.getValue() instanceof IDataNBTConvertable)
                compound.put(entry.getKey(), ((IDataNBTConvertable) entry.getValue()).toNbt());
        }
        return compound;
    }

    /**
     * Sets the value of a key.
     * @param key The key the data is to be set to.
     * @param value The data to be set.
     */
    @Override
    public void set(String key, IDataElement value) {
        contents.put(key, value);
    }

    /**
     * Creates a new data container and sets it to the given key.
     * @param key The key the data container is to be set to.
     * @return The newly created data container.
     */
    @Override
    public IDataContainer putContainer(String key) {
        IDataContainer data = new Data();
        contents.put(key, data);
        return data;
    }

    /**
     * Creates a new data string entry and sets it to the given key.
     * @param key The key the data string entry is to be set to.
     * @return The newly created data string entry.
     */
    @Override
    public IDataStringEntry putString(String key) {
        IDataStringEntry data = new DataStringEntry("");
        contents.put(key, data);
        return data;
    }

    /**
     * Gets the data element at the given key.
     * @param key The key the data element is stored under.
     * @return The data element, null if no data element is stored under the given key.
     */
    @Override
    public IDataElement get(String key) {
        if (contents.containsKey(key))
            return contents.get(key);

        // empty data
        return new Data();
    }


}
