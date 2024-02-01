package edu.kit.riscjblockits.model.instructionset;

//zwischen Programming und InstructionSet
public interface IQueryableInstruction {
    String[] getArguments();

    String[] getTranslation();

    IExecutableMicroInstruction[] getExecution();
}
