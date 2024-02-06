package edu.kit.riscjblockits.model.memoryrepresentation;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_ADDRESS;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_INITIAL_PC;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_MEMORY;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_WORD;

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
     * where the program counter should start.
     * TODO nicht im entwurf
     */
    private Value initialProgramCounter;

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
        // load word size and address size
        IDataStringEntry wordSizeString = (IDataStringEntry) data.get(MEMORY_WORD);
        IDataStringEntry addressSizeString = (IDataStringEntry) data.get(MEMORY_ADDRESS);

        int wordSize = Integer.parseInt(addressSizeString.getContent());
        int addressSize = Integer.parseInt(wordSizeString.getContent());

        // create new memory
        Memory memory = new Memory(addressSize, wordSize);

        // load initial program counter if one is set
        if (data.get(MEMORY_INITIAL_PC).isEntry()) {
            IDataStringEntry initialProgramCounterString = (IDataStringEntry) data.get(MEMORY_INITIAL_PC);
            memory.setInitialProgramCounter(Value.fromHex(initialProgramCounterString.getContent(), addressSize));
        }

        // fill memory with data
        IDataContainer memoryData = (IDataContainer) data.get("memory");
        for (String key : memoryData.getKeys()) {
            // get stored address and value
            IDataStringEntry value = (IDataStringEntry) memoryData.get(key);
            memory.setValue(Value.fromHex(key, addressSize), Value.fromHex(value.getContent(), wordSize));
        }
        return memory;
    }

    /**
     * Saves the memory to a {@link IDataElement}.
     * @return the saved memory
     *          Data Format: key: memory, value: DataContainer
     *                          key: wordSize, value: Memory length as String
     *                          key: addressSize, value: Address length as String
     */
    public IDataElement getData() {
        // create new data container
        IDataContainer data = new Data();

        // save word size and address size
        data.set(MEMORY_WORD, new DataStringEntry(String.valueOf(memoryLength)));
        data.set(MEMORY_ADDRESS, new DataStringEntry(String.valueOf(addressLength)));

        // save initial program counter if one is set
        if (initialProgramCounter != null)
            data.set(MEMORY_INITIAL_PC, new DataStringEntry(initialProgramCounter.getHexadecimalValue()));

        // create new data container for memory
        IDataContainer memoryData = new Data();

        // save all values
        for (Value address : memory.keySet()) {
            memoryData.set(address.getHexadecimalValue(), new DataStringEntry(memory.get(address).getHexadecimalValue()));
        }

        // save memory
        data.set(MEMORY_MEMORY, memoryData);
        return data;
    }

    // generated by wizard
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Memory memory1)) return false;
        return memoryLength == memory1.memoryLength && addressLength == memory1.addressLength && Objects.equals(memory, memory1.memory);
    }

    // generated by wizard
    @Override
    public int hashCode() {
        return Objects.hash(memory, memoryLength, addressLength);
    }

    /**
     * // TODO nicht im entwurf
     * @param address
     */
    public void setInitialProgramCounter(Value address) {
        initialProgramCounter = address;
    }

    /**
     * // TODO nicht im entwurf
     */
    public Value getInitialProgramCounter() {
        return initialProgramCounter;
    }

}
