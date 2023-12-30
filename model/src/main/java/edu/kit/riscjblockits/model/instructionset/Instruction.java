package edu.kit.riscjblockits.model.instructionset;

public class Instruction {
    private String[] arguments;

    public String getOpcode() {
        return opcode;
    }

    private String opcode;

    private MicroInstruction[] execution;

    private String[] translation;

    public Instruction(String[] arguments, String opcode, MicroInstruction[] execution, String[] translation) {
        this.arguments = arguments;
        this.opcode = opcode;
        this.execution = execution;
        this.translation = translation;
    }

    public String[] getTranslation() {
        return translation;
    }

    public String[] getArguments() {
        return arguments;
    }
}
