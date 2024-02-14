package edu.kit.riscjblockits.model.instructionset;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for common attributes and functionalities of complex micro instructions which can contain a
 * memory flag and memory instruction.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public abstract class ComplexMicroInstruction extends MicroInstruction{


    /**
     * Memory flag for type of memory access.
     */
    private final String memoryFlag;

    /**
     * Memory instruction for parallel memory access.
     */
    private MemoryInstruction memoryInstruction;

    public ComplexMicroInstruction(String[] from, String to, String memoryFlag, MemoryInstruction memoryInstruction) {
        super(from, to);
        this.memoryFlag = memoryFlag;
        this.memoryInstruction = memoryInstruction;
    }

    /**
     * Getter for the memory flag.
     * @return the memory flag as a string.
     */
    public String getMemoryFlag() {
        return memoryFlag;
    }

    /**
     * Getter for the memory instruction.
     * @return the memory instruction.
     */
    public MemoryInstruction getMemoryInstruction() {
        return memoryInstruction;
    }


    @Override
    public MicroInstruction getFilled(Map<String, String> argumentsInstructionMap, HashMap<Integer, String> intRegisters, HashMap<Integer, String> floatRegisters) {
        ComplexMicroInstruction filled = (ComplexMicroInstruction) super.getFilled(argumentsInstructionMap, intRegisters, floatRegisters);

        if (memoryInstruction != null)
            filled.memoryInstruction = (MemoryInstruction) filled.memoryInstruction.getFilled(argumentsInstructionMap, intRegisters, floatRegisters);

        return filled;
    }
}
