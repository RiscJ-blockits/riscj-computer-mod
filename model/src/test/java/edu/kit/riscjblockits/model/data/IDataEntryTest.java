package edu.kit.riscjblockits.model.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IDataEntryTest {

    @Test
    void isContainer() {
        assertFalse(new IDataEntry() {
            @Override
            public void receive(IDataVisitor visitor) {
                //
            }
            @Override
            public DataType getType() {
                return null;
            }
        }.isContainer());
    }

    @Test
    void isEntry() {
        assertTrue(new IDataEntry() {
            @Override
            public void receive(IDataVisitor visitor) {
                //
            }
            @Override
            public DataType getType() {
                return null;
            }
        }.isEntry());
    }

}
