package edu.kit.riscjblockits.model.instructionset;

//zwischen Simulation und InstructionSet
public interface IExecutableMicroInstruction {
    void execute(IExecutor executor);
}
