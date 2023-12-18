package edu.kit.riscjblockits.model.instructionset;

public class DataMovementInstruction extends ComplexMicroInstruction {

    public DataMovementInstruction(String[] from, String to, String memoryFlag, MemoryInstruction memoryInstruction) {
        super(from, to, memoryFlag, memoryInstruction);
    }
}
