package edu.kit.riscjblockits.model.data;

public interface IDataEntry extends IDataElement {
    @Override
    default boolean isContainer(){
        return false;
    }

    @Override
    default boolean isEntry() {
        return true;
    }

    /**
     * Will return the type of the entry.
     *
     * @return the type of the entry
     */
    DataType getType();
}
