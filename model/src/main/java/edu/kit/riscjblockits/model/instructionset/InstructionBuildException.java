package edu.kit.riscjblockits.model.instructionset;

/**
 * Exception that is thrown when an instruction set cannot be built.
 */
public class InstructionBuildException extends RuntimeException {
    /**
     * Constructor for the instruction build exception.
     * @param message The message of the exception.
     */
    public InstructionBuildException(String message) {
        super(message);
    }

}
