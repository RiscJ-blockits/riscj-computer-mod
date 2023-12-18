package edu.kit.riscjblockits.model.instructionset;

public class ConditionedInstruction extends ComplexMicroInstruction {

    InstructionCondition condition;

    public ConditionedInstruction(String[] from, String to, String memoryFlag,
                                  MemoryInstruction memoryInstruction, InstructionCondition condition) {
        super(from, to, memoryFlag, memoryInstruction);
        this.condition = condition;
    }
}
