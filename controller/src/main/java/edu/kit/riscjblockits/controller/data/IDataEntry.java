package edu.kit.riscjblockits.controller.data;

public interface IDataEntry extends IDataElement {
    @Override
    default boolean isContainer(){
        return false;
    }

    @Override
    default boolean isEntry() {
        return true;
    }

    DataType getType();
}
