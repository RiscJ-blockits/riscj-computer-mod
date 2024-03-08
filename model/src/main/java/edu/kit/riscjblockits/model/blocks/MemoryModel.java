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

    /**
     * Getter for one memory line in the memory block.
     * @param address the address of the memory line
     * @return the value of the memory line
     */
    public Value getMemoryAt(Value address) {
        if (memory == null) {
            return new Value();
        }
        return memory.getValueAt(address);
    }

    /**
     * Setter for one memory line in the memory.
     * Also sets the unqueried state change to true.
     * @param address the address of the memory line
     * @param value the value of the memory line
     */
    public void setMemoryAt(Value address, Value value) {
        memory.setValue(address, value);
        setUnqueriedStateChange(true);
    }

    /**
     * Setter for the memory of the memory block.
     * Also sets the unqueried state change to true.
     * @param memory the new memory
     * @return true if the memory was changed, false if it was not changed
     */
    public boolean setMemory(Memory memory) {
        if (this.memory == null || !this.memory.equals(memory)) {
            this.memory = memory;
            setUnqueriedStateChange(true);
            return true;
        }
        this.memory = memory;       //just to be safe
        setUnqueriedStateChange(true);
        return false;
    }

    /**
     * Getter for the initial program counter of the memory.
     * Returns null if the memory is not set.
     * @return the initial program counter of the memory as a value
     */
    public Value getInitialProgramCounter() {
        if (memory == null){
            return null;
        }
        return memory.getInitialProgramCounter();
    }

    public boolean isMemorySet() {
        return memory != null;
    }

    /**
     * Setter for the address we want to display in the memory screen.
     * @param line the line to set the memory query line to.
     */
    public void setMemoryQueryLine(long line) {
        if (memory == null) return;
        memory.setMemoryQueryLine(line);
    }

}
