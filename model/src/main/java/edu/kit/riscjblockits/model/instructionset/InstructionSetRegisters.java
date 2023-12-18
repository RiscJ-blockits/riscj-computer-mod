package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class InstructionSetRegisters {
    @SerializedName(value = "program_counter")
    String programCounter;
    @SerializedName(value = "alu")
    String[] aluRegs;
    @SerializedName(value = "float")
    HashMap<String, Integer> floatRegs;
    @SerializedName(value = "integer")
    HashMap<String, Integer> intRegs;

    public InstructionSetRegisters(String programCounter, String[] aluRegs, HashMap<String, Integer> floatRegs, HashMap<String, Integer> intRegs) {
        this.programCounter = programCounter;
        this.aluRegs = aluRegs;
        this.floatRegs = floatRegs;
        this.intRegs = intRegs;
    }

    public Integer getFloatRegister(String name) {
        return floatRegs.get(name);
    }

    public Integer getIntegerRegister(String name) {
        return intRegs.get(name);
    }
}
