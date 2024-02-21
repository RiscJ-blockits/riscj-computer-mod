package edu.kit.riscjblockits.model.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IDataStringEntryTest {

    @Test
    void getType() {
        assertEquals(DataType.STRING, new IDataStringEntry() {
            @Override
            public String getContent() {
                return null;
            }
            @Override
            public void setContent(String content) {
                //
            }
            @Override
            public void receive(IDataVisitor visitor) {
            }
        }.getType());
    }

}
