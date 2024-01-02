package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Model of an instruction set. Contains all information on how to execute code based on the instruction set.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class InstructionSetModel {

    /**
     * Name of the instruction set.
     */
    private final String name;

    /**
     * Length of the instructions in bits.
     */
    @SerializedName(value = "instruction_length")
    private final int instructionLength;

    /**
     * Register specifications of the instruction set.
     */
    @SerializedName(value = "registers")
    private final InstructionSetRegisters instructionSetRegisters;

    /**
     * Memory specifications of the instruction set.
     */
    @SerializedName(value = "memory")
    private final InstructionSetMemory instructionSetMemory;

    /**
     * ALU operations of the instruction set.
     */
    @SerializedName(value = "alu_operations")
    private final String[] aluActions;

    /**
     * Instructions of the instruction set, mapped by command keyword.
     */
    @SerializedName(value = "instructions")
    private final HashMap<String, Instruction> commandHashMap;

    /**
     * Instructions of the instruction set, mapped by opcode.
     */
    private HashMap<String, Instruction> opcodeHashMap;

    public InstructionSetModel() {
        this.name = null;
        this.instructionLength = 0;
        this.aluActions = null;
        this.commandHashMap = null;
        this.instructionSetMemory = null;
        this.instructionSetRegisters = null;
        this.opcodeHashMap = null;
    }

    public InstructionSetModel(String name, int instructionLength, InstructionSetRegisters instructionSetRegisters, InstructionSetMemory instructionSetMemory, String[] aluActions, HashMap<String, Instruction> commandHashMap, HashMap<String, Instruction> opcodeHashMap) {
        this.name = name;
        this.instructionLength = instructionLength;
        this.instructionSetRegisters = instructionSetRegisters;
        this.instructionSetMemory = instructionSetMemory;
        this.aluActions = aluActions;
        this.commandHashMap = commandHashMap;
        this.opcodeHashMap = opcodeHashMap;
    }

    /**
     * Generates the opcode hashmap from the command hashmap.
     */
    public void generateOpcodeHashmap(){
        this.opcodeHashMap = new HashMap<>();
        if(this.commandHashMap == null) return;
        this.commandHashMap.values().forEach(e -> opcodeHashMap.put(e.getOpcode(), e));
    }

    /**
     * Getter for the registers of the instruction set.
     * @return Registers of the instruction set.
     */
    public String[] getAluRegisters() {
        return instructionSetRegisters.aluRegs;
    }

    /**
     * Getter for the name of the instruction set.
     * @return Name of the instruction set.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the integer registers of the instruction set.
     * @param key Key of the register.
     * @return Integer register matching the key.
     */
    public Integer getIntegerRegister(String key) {
        return instructionSetRegisters.getIntegerRegister(key);
    }

    /**
     * Getter for the program counter register of the instruction set.
     * @return Program counter register of the instruction set.
     */
    public String getProgramCounter() {
        return instructionSetRegisters.getProgramCounter();
    }

}
