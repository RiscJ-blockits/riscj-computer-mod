package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.data.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MemoryControllerTest {

    MemoryController testMemoryController;

    @BeforeEach
    void setUp() {
        testMemoryController = new MemoryController(mock(IConnectableComputerBlockEntity.class));
    }

    @Test
    void createBlockModel() {
        assertNotNull(testMemoryController.createBlockModel());
    }

    @Test
    void setData() {
        /*Data data = new Data();
        testMemoryController.setData(data);
        assertEquals(data, testMemoryController.getModel().getData());*/
    }

    @Test
    void getValue() {
    }

    @Test
    void writeMemory() {
    }

    @Test
    void getInitialProgramCounter() {
    }

    @Test
    void isMemorySet() {
    }
}