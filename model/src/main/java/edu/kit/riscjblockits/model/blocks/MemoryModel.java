package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

public class MemoryModel extends BlockModel {

    private Memory memory;

    public MemoryModel() {
        setType(ModelType.MEMORY);
    }

    /**
     * Getter for the data the view needs for ui.
     * @return Data Format: key: memory, value: DataContainer
     *                                  key: wordSize, value: Memory length as String
     *                                  key: addressSize, value: Address length as String
     */
    @Override
    public IDataElement getData() {
        setUnqueriedStateChange(false);
        return memory.getData();
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
