package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.MemoryModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_MEMORY;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_PROGRAMM_ITEM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MemoryControllerTest {

    MemoryController testMemoryController;
    MemoryModel testMemoryModel;
    static Data data;

    @BeforeEach
    void setUp() {
        data = new Data();
        testMemoryController = new MemoryController(mock(IConnectableComputerBlockEntity.class));
        testMemoryModel = (MemoryModel) testMemoryController.getModel();
    }

    @Test
    void createBlockModel() {
        assertNotNull(testMemoryController.createBlockModel());
        assertInstanceOf(MemoryModel.class, testMemoryController.createBlockModel());
    }

    @Test
    void setNoData() {
        testMemoryController.setData(data);
        assertEquals(0, ((Data) testMemoryModel.getData()).getKeys().size());
    }

    @Test
    void setMemoryData() {
        Data memoryData = new Data();
        Memory memory = new Memory(2,2);
        memory.setValue(Value.fromHex("01",2),Value.fromHex("01",2));
        memory.setMemoryQueryLine(1);
        memoryData.set(MEMORY_PROGRAMM_ITEM, memory.getData());
        data.set(MEMORY_MEMORY, memoryData);
        testMemoryController.setData(data);
        assertEquals(3, ((Data) testMemoryModel.getData()).getKeys().size());
        data.set(MEMORY_MEMORY, null);
        testMemoryController.setData(data);
        assertEquals(0, ((Data) testMemoryModel.getData()).getKeys().size());
    }

    @Test
    void setNoMemoryData() {
        Data memoryData = new Data();
        memoryData.set(MEMORY_PROGRAMM_ITEM, new DataStringEntry("null"));
        data.set(MEMORY_MEMORY, memoryData);
        testMemoryController.setData(data);
        assertFalse(testMemoryModel.isMemorySet());
    }

    @Test
    void setNoMemoryData2() {
        data.set(MEMORY_MEMORY, new DataStringEntry("null"));
        testMemoryController.setData(data);
        assertFalse(testMemoryModel.isMemorySet());
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
