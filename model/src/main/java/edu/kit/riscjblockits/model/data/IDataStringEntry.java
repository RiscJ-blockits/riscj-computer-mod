package edu.kit.riscjblockits.model.data;

public interface IDataStringEntry extends IDataEntry {
    String getContent();
    void setContent(String content);

    @Override
    default DataType getType() {
        return DataType.STRING;
    }


}
