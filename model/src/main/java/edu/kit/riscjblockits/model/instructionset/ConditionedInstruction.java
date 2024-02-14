package edu.kit.riscjblockits.model.instructionset;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds a conditioned instruction like a jump or branch operation.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class ConditionedInstruction extends ComplexMicroInstruction {

    /**
     * Condition of the instruction.
     */
    private InstructionCondition condition;

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
    public MicroInstruction getFilled(Map<String, String> argumentsInstructionMap, HashMap<Integer, String> intRegisters, HashMap<Integer, String> floatRegisters) {
        ConditionedInstruction inst = (ConditionedInstruction) super.getFilled(argumentsInstructionMap, intRegisters, floatRegisters);
        inst.condition = new InstructionCondition(
            getFilledExecutionPart(condition.getComparator(), argumentsInstructionMap, intRegisters, floatRegisters),
            getFilledExecutionPart(condition.getCompare1(), argumentsInstructionMap, intRegisters, floatRegisters),
            getFilledExecutionPart(condition.getCompare2(), argumentsInstructionMap, intRegisters, floatRegisters)
        );
        return inst;
    }

    @Override
    public MicroInstruction clone(String[] from, String to) {
        return new ConditionedInstruction(from, to, getMemoryFlag(), getMemoryInstruction()==null ? null : getMemoryInstruction().clone(), condition);
    }
}
