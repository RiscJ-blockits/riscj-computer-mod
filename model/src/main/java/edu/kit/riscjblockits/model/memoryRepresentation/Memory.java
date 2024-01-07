package edu.kit.riscjblockits.model.memoryRepresentation;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the memory of the computer.
 * it is used to store {@link Value}s at addresses.
 * a memory instance has a fixed word- and address-size and can be written to and read from.
 */
public class Memory {

    /**
     * the actual memory.
     * a map of addresses to values.
     */
    private final Map<Value, Value> memory = new HashMap<>();

    /**
     * the size of a memory word in bytes.
     */
    private final int memoryLength;

    /**
     * the size of a memory address in bytes.
     */
    private final int addressLength;

    /**
     * Constructor for a memory.
     * will create a new memory with the given address and word size.
     * @param addressLength the size of a memory address in bytes
     * @param memoryLength the size of a memory word in bytes
     */
    public Memory(int addressLength, int memoryLength) {
        this.memoryLength = memoryLength;
        this.addressLength = addressLength;
    }

    /**
     * return the value at the given address.
     * will return a new zero value with the size of the memory word if the address is not set.
     * @param address the address to read from
     * @return the value at the given address
     */
    public Value getValueAt(Value address) {

        if (memory.containsKey(address))
            return memory.get(address);
        return new Value(new byte[memoryLength]);
    }

    /**
     * sets the value at the given address.
     * @param address the address to write to
     * @param value the value to write
     */
    public void setValue(Value address, Value value) {
        memory.put(address, value);
    }

    /**
     * Load a memory from a data container.
     * @param data the data container to load from
     * @return the loaded memory
     */
    public static Memory fromData(IDataContainer data) {
        return null;
    }

    /**
     * Saves the memory to a {@link IDataElement}.
     * @return the saved memory
     */
    public IDataElement getData() {
        return null;
    }
}
