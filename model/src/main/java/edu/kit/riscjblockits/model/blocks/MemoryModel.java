package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

/**
 * Represents the data and state of a memory. Every memory block has one.
 */
public class MemoryModel extends BlockModel {

    /**
     * Every memory block can hold one memory. It can be changed by inserting a new program item.
     */
    private Memory memory;

    /**
     * Constructor. Returns the model for a memory.
     */
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
        if (memory != null) {
            return memory.getData();
        }
        return new Data();
    }


    public Value getMemoryAt(Value address) {
        if (memory == null) {
            return new Value();
        }
        return memory.getValueAt(address);
    }

    public void setMemoryAt(Value address, Value value) {
        memory.setValue(address, value);
        setUnqueriedStateChange(true);
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
        setUnqueriedStateChange(true);
        System.out.println("Memory changed");
    }

    public Value getInitialProgramCounter() {
        if (memory == null){
            return null;
        }
        return memory.getInitialProgramCounter();
    }

    public boolean isMemorySet() {
        return memory != null;
    }
}
