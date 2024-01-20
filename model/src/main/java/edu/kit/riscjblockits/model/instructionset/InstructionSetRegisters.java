package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Model of instruction set registers. Contains all information on the registers of the instruction set.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class InstructionSetRegisters {

    /**
     * Program counter register name.
     */
    @SerializedName(value = "program_counter")
    private final String programCounter;

    /**
     * ALU register names.
     */
    @SerializedName(value = "alu")
    private final String[] aluRegs;

    /**
     * Floating point register mapping of addresses and names.
     */
    @SerializedName(value = "float")
    private final HashMap<String, Integer> floatRegs;

    /**
     * Integer register mapping of addresses and names.
     */
    @SerializedName(value = "integer")
    private final HashMap<String, Integer> intRegs;

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

    /**
     * Getter for the ALU registers.
     * @return ALU registers.
     */
    public String[] getAluRegs() {
        return aluRegs;
    }

    /**
     * ToDo nicht im Entwurf
     * @return Returns the names of all registers.
     */
    public List<String> getRegisterNames() {
        //ToDo assert: no two integers have the same name
        List<String> names = new ArrayList<>(Arrays.asList(aluRegs));
        names.add(programCounter);
        names.addAll(intRegs.keySet());
        names.addAll(floatRegs.keySet());
        return names;
    }
}
