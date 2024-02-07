package edu.kit.riscjblockits.model.instructionset;

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
    private final MemoryInstruction memoryInstruction;

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

}
