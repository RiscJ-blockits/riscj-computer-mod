package edu.kit.riscjblockits.model.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataTest {

    @Test
    void remove() {
        Data testData = new Data();
        testData.set("testKey", new DataStringEntry("testValue"));
        testData.remove("testKey");
        assertEquals(0, testData.getKeys().size());
    }

}
