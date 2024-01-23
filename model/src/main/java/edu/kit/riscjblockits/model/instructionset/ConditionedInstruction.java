package edu.kit.riscjblockits.model.instructionset;

/**
 * Holds a conditioned instruction like a jump or branch operation.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class ConditionedInstruction extends ComplexMicroInstruction {

    /**
     * Condition of the instruction.
     */
    private final InstructionCondition condition;

    public ConditionedInstruction(String[] from, String to, String memoryFlag,
                                  MemoryInstruction memoryInstruction, InstructionCondition condition) {
        super(from, to, memoryFlag, memoryInstruction);
        this.condition = condition;
    }

    /**
     * Hands the conditioned instruction to the executor and executes it as part of a visitor pattern.
     */
    @Override
    public void execute(IExecutor executor) {
        executor.execute(this);
    }

    /**
     * Getter for the condition of the instruction.
     * @return The condition of the instruction.
     */
    public InstructionCondition getCondition() {
        return condition;
    }

    @Override
    public MicroInstruction clone(String[] from, String to) {
        return new ConditionedInstruction(from, to, getMemoryFlag(), getMemoryInstruction().clone(), condition);
    }
}
