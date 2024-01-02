package edu.kit.riscjblockits.model.instructionset;

/**
 * Represents an instruction of the instruction set.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class Instruction {

    /**
     * The arguments of the instruction.
     */
    String[] arguments;

    /**
     * The opcode of the instruction.
     */
    String opcode;

    /**
     * The execution of the instruction as a sequence of micro instructions.
     */
    MicroInstruction[] execution;

    /**
     * The translation of the instruction to binary.
     */
    String[] translation;

    public Instruction(String[] arguments, String opcode, MicroInstruction[] execution, String[] translation) {
        this.arguments = arguments;
        this.opcode = opcode;
        this.execution = execution;
        this.translation = translation;
    }

    /**
     * Getter for the opcode of the instruction.
     * @return The opcode of the instruction as a string.
     */
    public String getOpcode() {
        return opcode;
    }
}
