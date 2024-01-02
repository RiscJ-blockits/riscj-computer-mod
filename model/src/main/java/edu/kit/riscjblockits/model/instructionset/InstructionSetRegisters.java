package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Model of instruction set registers. Contains all information on the registers of the instruction set.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class InstructionSetRegisters {

    /**
     * Program counter register name.
     */
    @SerializedName(value = "program_counter")
    String programCounter;

    /**
     * ALU register names.
     */
    @SerializedName(value = "alu")
    String[] aluRegs;

    /**
     * Floating point register mapping of addresses and names.
     */
    @SerializedName(value = "float")
    HashMap<String, Integer> floatRegs;

    /**
     * Integer register mapping of addresses and names.
     */
    @SerializedName(value = "integer")
    HashMap<String, Integer> intRegs;

    public InstructionSetRegisters(String programCounter, String[] aluRegs, HashMap<String, Integer> floatRegs, HashMap<String, Integer> intRegs) {
        this.programCounter = programCounter;
        this.aluRegs = aluRegs;
        this.floatRegs = floatRegs;
        this.intRegs = intRegs;
    }

    /**
     * Getter for the floating point registers.
     * @param name Name of the register.
     * @return Address of the register.
     */
    public Integer getFloatRegister(String name) {
        return floatRegs.get(name);
    }

    /**
     * Getter for the integer registers.
     * @param name Name of the register.
     * @return Address of the register.
     */
    public Integer getIntegerRegister(String name) {
        return intRegs.get(name);
    }

    /**
     * Getter for the program counter register.
     * @return Name of the program counter register.
     */
    public String getProgramCounter() {
        return programCounter;
    }
}
