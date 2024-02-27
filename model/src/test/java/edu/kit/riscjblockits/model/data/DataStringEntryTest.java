package edu.kit.riscjblockits.model.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataStringEntryTest {

    @Test
    void setContent() {
        DataStringEntry dataStringEntry = new DataStringEntry("Test");
        assertEquals("Test", dataStringEntry.getContent());
        dataStringEntry.setContent("Test2");
        assertEquals("Test2", dataStringEntry.getContent());
    }

}
