package edu.kit.riscjblockits.model.instructionset;

/**
 * Abstract class for common attributes and functionality of micro instructions.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public abstract class MicroInstruction implements IExecutableMicroInstruction {

    /**
     * The register(s) to read from.
     */
    private final String[] from;

    /**
     * The register to write to.
     */
    private final String to;

    public MicroInstruction(String[] from, String to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Getter method for the registers to read from.
     * @return The registers to read from.
     */
    public String[] getFrom() {
        return from;
    }

    /**
     * Getter method for the register to write to.
     * @return The register to write to.
     */
    public String getTo() {
        return to;
    }



}
