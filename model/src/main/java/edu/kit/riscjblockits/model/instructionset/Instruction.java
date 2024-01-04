package edu.kit.riscjblockits.model.instructionset;

/**
 * Represents an instruction of the instruction set.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class Instruction implements IQueryableInstruction {

    /**
     * The arguments of the instruction.
     */
    private String[] arguments;

    /**
     * The opcode of the instruction.
     */
    private String opcode;

    /**
     * The execution of the instruction as a sequence of micro instructions.
     */
    private MicroInstruction[] execution;

    /**
     * The translation of the instruction to binary.
     */
    private String[] translation;

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


    /**
     * Getter for the binary translation of the instruction.
     * @return The binary translation of the instruction as a string array.
     */
    public String[] getTranslation() {
        return translation;
    }

    /**
     * Getter for the arguments of the instruction.
     * @return The arguments of the instruction as a string array.
     */
    public String[] getArguments() {
        return arguments;
    }
}
