package edu.kit.riscjblockits.model.instructionset;

/**
 * Holds a data movement instruction like a register load or store operation.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class DataMovementInstruction extends ComplexMicroInstruction {

    public DataMovementInstruction(String[] from, String to, String memoryFlag, MemoryInstruction memoryInstruction) {
        super(from, to, memoryFlag, memoryInstruction);
    }

    /**
     * Hands the data movement instruction to the executor and executes it as part of a visitor pattern.
     */
    @Override
    public void execute(IExecutor executor) {
        executor.execute(this);
    }
}
