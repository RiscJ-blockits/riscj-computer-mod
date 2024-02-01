package edu.kit.riscjblockits.model.instructionset;

/**
 * Holds an alu instruction like an arithmetics operation.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class AluInstruction extends ComplexMicroInstruction {

    /**
     * Alu action of the instruction.
     */
    private final String action;

    public AluInstruction(String[] from, String to, String memoryFlag, MemoryInstruction memoryInstruction, String action) {
        super(from, to, memoryFlag, memoryInstruction);
        this.action = action;
    }

    /**
     * Returns the alu action of the instruction.
     * @return the alu action of the instruction as a string.
     */
    public String getAction() {
        return action;
    }

    /**
     * Hands the alu instruction to the executor and executes it as part of a visitor pattern.
     */
    @Override
    public void execute(IExecutor executor) {
        executor.execute(this);
    }

    @Override
    public MicroInstruction clone(String[] from, String to) {
        return new AluInstruction(from, to, getMemoryFlag(), getMemoryInstruction().clone(), action);
    }
}
