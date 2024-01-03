package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.Memory;
import edu.kit.riscjblockits.model.Value;
import edu.kit.riscjblockits.model.data.IDataElement;

public class MemoryModel extends BlockModel {

    private Memory memory;

    @Override
    public boolean hasUnqueriedStateChange() {
        return false;
    }

    @Override
    public void writeDataRequest(IDataElement dataElement) {

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
