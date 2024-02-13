package edu.kit.riscjblockits.model.data;

import java.util.Set;

public interface IDataContainer extends IDataElement {
    @Override
    default boolean isContainer(){
        return true;
    }

    @Override
    default boolean isEntry() {
        return false;
    }

    /**
     * Will return the Element stored under the given key.
     *
     * @param key the key of the element
     * @return the element under the given key
     */
    IDataElement get(String key);

    /**
     * Will store the given element under the given key.
     *
     * @param key the key of the element
     * @param value the element to be stored
     */
    void set(String key, IDataElement value);

    /**
     * Will return a set of all keys stored in this container.
     *
     * @return a set of all keys stored in this container
     */
    Set<String> getKeys();

    void remove(String key);

}
