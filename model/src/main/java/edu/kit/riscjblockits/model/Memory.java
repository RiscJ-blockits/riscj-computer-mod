package edu.kit.riscjblockits.model;

import java.util.HashMap;
import java.util.Map;

public class Memory {

private final Map<Value, Value> memory = new HashMap<>();
    private final int memoryLength;
    private final int addressLength;

    public Memory(int addressLength, int memoryLength) {
        this.memoryLength = memoryLength;
        this.addressLength = addressLength;
    }

    public Value getValueAt(Value address) {

        if (memory.containsKey(address))
            return memory.get(address);
        return new Value(new byte[memoryLength]);
    }

    public void setValue(Value address, Value value) {
        memory.put(address, value);
    }


}
