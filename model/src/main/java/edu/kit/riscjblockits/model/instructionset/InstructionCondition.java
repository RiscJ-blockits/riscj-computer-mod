package edu.kit.riscjblockits.model.instructionset;

/**
 * Represents a condition for a conditioned micro instruction.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class InstructionCondition {

    /**
     * The comparing oparator.
     */
    private final String comparator;

    /**
     * The first value to compare.
     */
    private final String compare1;

    /**
     * The second value to compare.
     */
    private final String compare2;

    /**
     * Constructor for the instruction condition.
     * @param comparator The comparing operator.
     * @param compare1 The first value to compare.
     * @param compare2 The second value to compare.
     */
    public InstructionCondition(String comparator, String compare1, String compare2) {
        this.comparator = comparator;
        this.compare1 = compare1;
        this.compare2 = compare2;
    }


    /**
     * Getter for the comparing operator.
     * @return The comparing operator as a string.
     */
    public String getComparator() {
        return comparator;
    }

    /**
     * Getter for the first value to compare.
     * @return The first value to compare as a string.
     */
    public String getCompare1() {
        return compare1;
    }

    /**
     * Getter for the second value to compare.
     * @return The second value to compare as a string.
     */
    public String getCompare2() {
        return compare2;
    }
}
