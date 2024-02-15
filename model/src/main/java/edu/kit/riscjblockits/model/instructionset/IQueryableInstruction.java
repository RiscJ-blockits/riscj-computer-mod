package edu.kit.riscjblockits.model.instructionset;

/**
 * This interface defines the methods that an Instruction provides to assemble some Code.
 */
public interface IQueryableInstruction {
    /**
     * Getter for the Arguments of the Instruction.
     * @return The Arguments of the Instruction.
     */
    String[] getArguments();

    /**
     * Getter for the Translation of the Instruction.
     * @return The Translation of the Instruction.
     */
    String[] getTranslation();

    /**
     * Getter for the ExecutableMicroInstruction of the Instruction.
     * @return The ExecutableMicroInstruction of the Instruction.
     */
    IExecutableMicroInstruction[] getExecution();
}
