package edu.kit.riscjblockits.model.instructionset;

/**
 * This interface defines the methods that an executable micro instruction provides.
 */
public interface IExecutableMicroInstruction {

    /**
     * Abstract method to execute the micro instruction as part of a visitor pattern.
     * @param executor The executor to execute the micro instruction on.
     */
    void execute(IExecutor executor);

    /**
     * Abstract method to clone the micro instruction.
     * @param from The source register.
     * @param to The destination register.
     * @return The cloned micro instruction.
     */
    MicroInstruction clone(String[] from, String to);
}
