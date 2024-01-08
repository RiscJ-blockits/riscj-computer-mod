package edu.kit.riscjblockits.model;

import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class MemoryTest {

    @Test
    void getAndSetValueAt() {
        Memory memory = new Memory(4, 4);

        Value val = new Value(new byte[]{12,32,43,10});
        memory.setValue(new Value(new byte[]{0,0,0,0x10}), val);

        Value v = memory.getValueAt(new Value(new byte[]{0,0,0,0x10}));

        assertArrayEquals(val.getByteValue(), v.getByteValue());


    }

}