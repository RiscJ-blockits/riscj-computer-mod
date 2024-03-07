package edu.kit.riscjblockits.model.memoryrepresentation;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_ADDRESS;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_INITIAL_PC;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_MEMORY;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_MEMORYSIZE;
import static edu.kit.riscjblockits.model.data.DataConstants.MEMORY_WORD;

/**
 * This class represents the memory of the computer.
 * It is used to store {@link Value}s at addresses.
 * A memory instance has a fixed word- and address-size and can be written to and read from.
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
     * the size of the memory in bits.
     */
    private final int memorySize;

    /**
     * where the program counter should start.
     */
    private Value initialProgramCounter;
    private long queryLine;

    /**
     * Constructor for memory.
     * Will create a new memory with the given address and word size.
     * @param addressLength the size of a memory address in bytes
     * @param memoryLength the size of a memory word in bytes
     */
    public Memory(int addressLength, int memoryLength, int memorySize) {
        this.memoryLength = memoryLength;
        this.addressLength = addressLength;
        this.memorySize = memorySize;
    }

    /**
     * return the value at the given address.
     * will return a new zero value with the size of the memory word if the address is not set.
     * @param address the address to read from
     * @return the value at the given address
     */
    public Value getValueAt(Value address) {
        Value trimmedAddress = getTrimmedAddress(address);
        if (memory.containsKey(trimmedAddress))
            return memory.get(trimmedAddress);
        return new Value(new byte[memoryLength]);
    }

    /**
     * sets the value at the given address.
     * @param address the address to write to
     * @param value the value to write
     */
    public void setValue(Value address, Value value) {
        Value trimmedAddress = getTrimmedAddress(address);
        synchronized (memory) {
            memory.put(trimmedAddress, value);
        }
    }

    /**
     * Will trim a given address to the memory address size.
     * @param address the address to trim
     * @return the trimmed address
     */
    private Value getTrimmedAddress(Value address) {
        String addressString = address.getBinaryValue();
        if (addressString.length() <= memorySize)
            return Value.fromBinary(addressString, (memorySize / 8) + (memorySize % 8 == 0? 0 : 1));

        return Value.fromBinary(addressString.substring(addressString.length() - memorySize),
                (memorySize / 8) + (memorySize % 8 == 0? 0 : 1));
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
        IDataStringEntry memorySizeString = (IDataStringEntry) data.get(MEMORY_MEMORYSIZE);

        int wordSize = Integer.parseInt(addressSizeString.getContent());
        int addressSize = Integer.parseInt(wordSizeString.getContent());
        int memorySize = Integer.parseInt(memorySizeString.getContent());

        // create new memory
        Memory memory = new Memory(addressSize, wordSize, memorySize);

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
     * Only sends 500 addresses around the query line.
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
        data.set(MEMORY_MEMORYSIZE, new DataStringEntry(String.valueOf(memorySize)));

        // save initial program counter if one is set
        if (initialProgramCounter != null)
            data.set(MEMORY_INITIAL_PC, new DataStringEntry(initialProgramCounter.getHexadecimalValue()));

        // create new data container for memory
        IDataContainer memoryData = new Data();
        // synchronize memory to avoid concurrent modification
        Set<Value> values;
        synchronized (memory) {
            values = new HashSet<>(memory.keySet());
        }
        // save all values
        for (Value address : values) {
            long addressInt = new BigInteger(address.getByteValue()).intValue();
            if (!((addressInt > (queryLine - 500)) && addressInt < queryLine + 500)) {
                continue;
            }
            Value value = memory.get(address);
            if(value == null) continue;
            memoryData.set(address.getHexadecimalValue(), new DataStringEntry(value.getHexadecimalValue()));
        }
        // save memory
        data.set(MEMORY_MEMORY, memoryData);
        return data;
    }

    /**
     * Saves the memory to a {@link IDataElement}.
     * Saves the whole memory.
     * @return the saved memory
     *          Data Format: key: memory, value: DataContainer
     *                          key: wordSize, value: Memory length as String
     *                          key: addressSize, value: Address length as String
     */
    public IDataElement getCompleteData() {
        IDataContainer data = (IDataContainer) getData();
        IDataContainer memoryData = new Data();
        Set<Value> values;
        values = new HashSet<>(memory.keySet());
        // save all values
        for (Value address : values) {
            Value value = memory.get(address);
            if(value == null) continue;
            memoryData.set(address.getHexadecimalValue(), new DataStringEntry(value.getHexadecimalValue()));
        }
        data.set(MEMORY_MEMORY, memoryData);
        return data;
    }

    @Override
    public boolean equals(Object o) { // generated by wizard
        if (this == o) return true;
        if (!(o instanceof Memory memory1)) return false;
        return memorySize == memory1.memorySize && memoryLength == memory1.memoryLength && addressLength == memory1.addressLength && Objects.equals(memory, memory1.memory);
    }

    @Override
    public int hashCode() { // generated by wizard
        return Objects.hash(memory, memoryLength, addressLength);
    }

    /**
     * Sets the initial program counter. This is the address the program should start at.
     * @param address the address to set the initial program counter to.
     */
    public void setInitialProgramCounter(Value address) {
        initialProgramCounter = address;
    }

    /**
     * Getter for the initial program counter.
     * @return the initial program counter.
     */
    public Value getInitialProgramCounter() {
        return initialProgramCounter;
    }

    public void setMemoryQueryLine(long line) {
        queryLine = line;
    }

}
