package edu.kit.riscjblockits.model.instructionset;

public class Instruction {
    String[] arguments;

    public String getOpcode() {
        return opcode;
    }

    String opcode;

    MicroInstruction[] execution;

    String[] translation;

    public Instruction(String[] arguments, String opcode, MicroInstruction[] execution, String[] translation) {
        this.arguments = arguments;
        this.opcode = opcode;
        this.execution = execution;
        this.translation = translation;
    }
}
