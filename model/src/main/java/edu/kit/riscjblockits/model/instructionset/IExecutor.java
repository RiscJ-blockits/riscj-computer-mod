package edu.kit.riscjblockits.model.instructionset;

/**
 * This interface defines the methods that an executor provides.
 */
public interface IExecutor {
    /**
     * Will execute the given micro instruction.
     * @param memoryInstruction The micro instruction to be executed.
     */
    void execute(MemoryInstruction memoryInstruction);

    /**
     * Will execute the given micro instruction.
     * @param conditionedInstruction The micro instruction to be executed.
     */
    void execute(ConditionedInstruction conditionedInstruction);

    /**
     * Will execute the given micro instruction.
     * @param aluInstruction  The micro instruction to be executed.
     */
    void execute(AluInstruction aluInstruction);

    /**
     * Will execute the given micro instruction.
     * @param dataMovementInstruction The micro instruction to be executed.
     */
    void execute(DataMovementInstruction dataMovementInstruction);
}
