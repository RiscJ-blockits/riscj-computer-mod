package edu.kit.riscjblockits.model.instructionset;

public class AluInstruction extends ComplexMicroInstruction {

    String action;

    public AluInstruction(String[] from, String to, String memoryFlag, MemoryInstruction memoryInstruction, String action) {
        super(from, to, memoryFlag, memoryInstruction);
        this.action = action;
    }


    public String getAction() {
        return action;
    }
}
