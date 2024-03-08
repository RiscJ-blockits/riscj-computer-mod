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

    /**
     * Map holding the Initial values of registers that are to have one.
     */
    @SerializedName(value = "initial_values")
    private final HashMap<String, String> initialValues;

    private HashMap<Integer, String> intRegisterAddressMap;
    private HashMap<Integer, String> floatRegisterAddressMap;

    /**
     * Constructor for the instruction set registers.
     * Here all available registers are stored.
     * @param programCounter The program counter register name.
     * @param aluRegs The ALU register names.
     * @param floatRegs The floating point register mapping of addresses and names.
     * @param intRegs The integer register mapping of addresses and names.
     * @param initialValues The initial values of registers that are to have one.
     */
    public InstructionSetRegisters(String programCounter, String[] aluRegs, HashMap<String, Integer> floatRegs, HashMap<String, Integer> intRegs,
                                   HashMap<String, String> initialValues) {
        this.programCounter = programCounter;
        this.aluRegs = aluRegs;
        this.floatRegs = floatRegs;
        this.intRegs = intRegs;
        this.initialValues = initialValues;
    }

    /**
     * Generates the register address maps from the register maps.
     */
    public void generateRegisterAddressMaps() {
        intRegisterAddressMap = generateRegisterAddressMap(intRegs);
        floatRegisterAddressMap = generateRegisterAddressMap(floatRegs);
    }

    /**
     * Getter for the floating point registers.
     * @param name Name of the register.
     * @return Address of the register.
     */
    public Integer getFloatRegister(String name) {
        if (!floatRegs.containsKey(name)) {
            return null;
        }
        return floatRegs.get(name);
    }

    /**
     * Getter for the integer registers.
     * @param name Name of the register.
     * @return Address of the register.
     */
    public Integer getIntegerRegister(String name) {
        if (!intRegs.containsKey(name)) {
            return null;
        }
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
     * Collects and returns the names of all registers.
     * @return Returns the names of all registers.
     */
    public List<String> getRegisterNames() {
        //ToDo assert: no two registers have the same name
        //ToDo assert: no register names have spaces in them
        //ToDo [NOT ASSIGNED] is not a valid register name
        List<String> names = new ArrayList<>(Arrays.asList(aluRegs));
        names.add(programCounter);
        names.addAll(intRegs.keySet());
        names.addAll(floatRegs.keySet());
        return names;
    }

    /**
     * Some registers have initial values. This method returns the initial value of a register.
     * @param key The name of the register.
     * @return The initial value of the register.
     */
    public String getInitialValue(String key) {
        if (initialValues == null)
            return null;
        return initialValues.get(key);
    }

    private HashMap<Integer, String> generateRegisterAddressMap(HashMap<String, Integer> registers) {
        HashMap<Integer, String> map = new HashMap<>();
        for (String key : registers.keySet()) {
            map.put(registers.get(key), key);
        }
        return map;
    }

    public HashMap<Integer, String> getIntRegisterAddressMap() {
        return intRegisterAddressMap;
    }

    public HashMap<Integer, String> getFloatRegisterAddressMap() {
        return floatRegisterAddressMap;
    }
}
