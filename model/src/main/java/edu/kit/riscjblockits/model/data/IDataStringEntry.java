package edu.kit.riscjblockits.model.data;

public interface IDataStringEntry extends IDataEntry {

    /**
     * Will return the content of the entry.
     *
     * @return the content of the entry
     */
    String getContent();

    /**
     * Will set the content of the entry.
     *
     * @param content the content to be set
     */
    void setContent(String content);

    @Override
    default DataType getType() {
        return DataType.STRING;
    }


}
