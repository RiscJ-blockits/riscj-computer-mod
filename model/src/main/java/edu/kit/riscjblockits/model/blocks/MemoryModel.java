package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.Memory;
import edu.kit.riscjblockits.model.Value;

public class MemoryModel extends BlockModel {

    private Memory memory;

    @Override
    public boolean getHasUnqueriedStateChange() {
        return false;
    }

    public Value getMemoryAt(Value address) {
        return memory.getValueAt(address);
    }

    public void setMemoryAt(Value address, Value value) {
        memory.setValue(address, value);
    }
    public void setMemory(Memory memory) {
        this.memory = memory;
    }
}
