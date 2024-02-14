package edu.kit.riscjblockits.model.data;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is a data container that can be converted to and from NBT.
 * It is used to convert Minecraft's NBT data to our own data format.
 */
public class Data implements IDataContainer {

    /**
     * The contents of this data container.
     * The Map's key is the key the data is stored under.
     * The value is the {@link IDataElement} itself.
     */
    private final Map<String, IDataElement> contents = new HashMap<>();

    /**
     * Creates a new empty Data container.
     */
    public Data () {
        //create empty data
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


    /**
     * Receives a {@link IDataVisitor} and calls the visit method on it.
     * @param visitor The visitor to be visited.
     */
    @Override
    public void receive(IDataVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Gets the keys of the data container.
     * @return The keys of the data container.
     */
    public Set<String> getKeys() {
        return contents.keySet();
    }

    public void remove(String key) {
        contents.remove(key);
    }

}
