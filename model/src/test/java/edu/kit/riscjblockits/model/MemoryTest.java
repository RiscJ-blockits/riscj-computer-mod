package edu.kit.riscjblockits.model;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MemoryTest {

    @Test
    void getAndSetValueAt() {
        Memory memory = new Memory(4, 4);

        Value val = new Value(new byte[]{12,32,43,10});
        memory.setValue(new Value(new byte[]{0,0,0,0x10}), val);

        Value v = memory.getValueAt(new Value(new byte[]{0,0,0,0x10}));

        assertArrayEquals(val.getByteValue(), v.getByteValue());


    }

    @Test
    void testLoad() {
        Memory memory = new Memory(2, 2);

        Value val = new Value(new byte[]{1,32});
        memory.setValue(new Value(new byte[]{0,0x10}), val);

        IDataContainer data = new Data();
        data.set("wordSize", new DataStringEntry("2"));
        data.set("addressSize", new DataStringEntry("2"));

        IDataContainer memoryData = new Data();
        memoryData.set("10", new DataStringEntry("0120"));

        data.set("memory", memoryData);

        Memory loadedMemory = Memory.fromData(data);

        assertEquals(memory, loadedMemory);
    }

    @Test
    void testStore() {
        Memory memory = new Memory(2, 2);

        Value val = new Value(new byte[]{1,32});
        memory.setValue(new Value(new byte[]{0,0x10}), val);

        IDataContainer data = (IDataContainer) memory.getData();

        ((IDataContainer) data.get("memory")).set("11", new DataStringEntry("0130"));

        val = new Value(new byte[]{1,48});
        memory.setValue(new Value(new byte[]{0,0x11}), val);

        Memory loadedMemory = Memory.fromData(data);

        assertEquals(memory, loadedMemory);
    }

}