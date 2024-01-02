package edu.kit.riscjblockits.model.instructionset;

/**
 * Represents a condition for a conditioned micro instruction.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class InstructionCondition {

    /**
     * The comparing oparator.
     */
    String comparator;

    /**
     * The first value to compare.
     */
    String compare1;

    /**
     * The second value to compare.
     */
    String compare2;

    public InstructionCondition(String comparator, String compare1, String compare2) {
        this.comparator = comparator;
        this.compare1 = compare1;
        this.compare2 = compare2;
    }
}
