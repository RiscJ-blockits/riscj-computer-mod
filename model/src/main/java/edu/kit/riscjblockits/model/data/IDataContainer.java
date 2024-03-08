package edu.kit.riscjblockits.model.data;

import java.util.Set;

/**
 * This interface defines the methods that a data container provides.
 */
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

    /**
     * Will remove the element stored under the given key.
     * @param key the key of the element to be removed
     */
    void remove(String key);

}
