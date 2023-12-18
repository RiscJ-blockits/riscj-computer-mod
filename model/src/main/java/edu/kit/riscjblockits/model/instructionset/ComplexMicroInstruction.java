package edu.kit.riscjblockits.model.instructionset;

public abstract class ComplexMicroInstruction extends MicroInstruction{


    String memoryFlag;
    MemoryInstruction memoryInstruction;

    public ComplexMicroInstruction(String[] from, String to, String memoryFlag, MemoryInstruction memoryInstruction) {
        super(from, to);
        this.memoryFlag = memoryFlag;
        this.memoryInstruction = memoryInstruction;
    }

    public String getMemoryFlag() {
        return memoryFlag;
    }

    public MemoryInstruction getMemoryInstruction() {
        return memoryInstruction;
    }

}
