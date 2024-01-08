package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import edu.kit.riscjblockits.model.data.IDataElement;

public class MemoryModel extends BlockModel {

    private Memory memory;

    public MemoryModel() {
        setType(ModelType.MEMORY);
    }

    @Override
    public boolean hasUnqueriedStateChange() {
        return false;
    }

    @Override
    public IDataElement getData() {
        return null;
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
