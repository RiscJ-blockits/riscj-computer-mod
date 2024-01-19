package edu.kit.riscjblockits.model.instructionset;

public interface IExecutor {
    void execute(MemoryInstruction memoryInstruction);

    void execute(ConditionedInstruction conditionedInstruction);

    void execute(AluInstruction aluInstruction);

    void execute(DataMovementInstruction dataMovementInstruction);
}
