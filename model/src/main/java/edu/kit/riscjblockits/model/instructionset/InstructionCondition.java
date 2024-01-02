package edu.kit.riscjblockits.model.instructionset;

public class InstructionCondition {
    String comparator;
    String compare1;
    String compare2;

    public InstructionCondition(String comparator, String compare1, String compare2) {
        this.comparator = comparator;
        this.compare1 = compare1;
        this.compare2 = compare2;
    }
}
