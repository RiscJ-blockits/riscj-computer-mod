package edu.kit.riscjblockits.controller.data;

public interface IDataStringEntry extends IDataEntry {
    String getContent();
    void setContent(String content);

    @Override
    default DataType getType() {
        return DataType.STRING;
    }


}
