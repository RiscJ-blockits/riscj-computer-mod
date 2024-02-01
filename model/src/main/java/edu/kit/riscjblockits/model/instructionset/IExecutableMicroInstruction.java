package edu.kit.riscjblockits.model.instructionset;

//zwischen Simulation und InstructionSet
public interface IExecutableMicroInstruction {

    /**
     * Abstract method to execute the micro instruction as part of a visitor pattern.
     * @param executor The executor to execute the micro instruction on.
     */
    void execute(IExecutor executor);

    MicroInstruction clone(String[] from, String to);
}
