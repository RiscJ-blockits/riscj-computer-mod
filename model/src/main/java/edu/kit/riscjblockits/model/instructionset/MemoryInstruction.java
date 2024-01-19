package edu.kit.riscjblockits.model.instructionset;

/**
 * Represents a memory action as part of a micro instruction.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class MemoryInstruction extends MicroInstruction {

    public MemoryInstruction(String[] from, String to) {
        super(from, to);
    }

    /**
     * Hands the memory action to the executor and executes it as part of a visitor pattern.
     */
    @Override
    public void execute(IExecutor executor) {
        executor.execute(this);
    }
}
