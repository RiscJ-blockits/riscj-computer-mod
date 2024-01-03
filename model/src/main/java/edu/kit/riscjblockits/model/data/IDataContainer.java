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

    IDataElement get(String key);

    void set(String key, IDataElement value);

    IDataContainer putContainer(String key);

    IDataStringEntry putString(String key);
}
