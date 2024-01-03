package edu.kit.riscjblockits.model.data;

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
     * Will add a new container under the given key.
     *
     * @param key the key the container is to be stored under
     * @return the new container
     */
    IDataContainer putContainer(String key);

    /**
     * Will add a new String entry under the given key.
     *
     * @param key the key the String entry is to be stored under
     * @return the new String entry
     */
    IDataStringEntry putString(String key);
}
