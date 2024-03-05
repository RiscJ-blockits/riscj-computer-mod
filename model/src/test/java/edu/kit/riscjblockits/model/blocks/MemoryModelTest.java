package edu.kit.riscjblockits.model.blocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryModelTest {

    MemoryModel memoryModel;

    @BeforeEach
    void init() {
        memoryModel = new MemoryModel();
    }

    @Test
    void getNullMemoryAt() {
        assertEquals("", memoryModel.getMemoryAt(null).getHexadecimalValue());
    }

}
