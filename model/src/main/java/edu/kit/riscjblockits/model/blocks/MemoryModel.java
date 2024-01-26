package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

public class MemoryModel extends BlockModel {

    private Memory memory;

    public MemoryModel() {
        setType(ModelType.MEMORY);
    }

    @Override
    public IDataElement getData() {
        Data memData = new Data();
        //ToDo

        setUnqueriedStateChange(false);
        return memData;
    }

    public Value getMemoryAt(Value address) {
        return memory.getValueAt(address);
    }

    public void setMemoryAt(Value address, Value value) {
        memory.setValue(address, value);
        setUnqueriedStateChange(true);
    }
    public void setMemory(Memory memory) {
        this.memory = memory;
        setUnqueriedStateChange(true);
    }

}
