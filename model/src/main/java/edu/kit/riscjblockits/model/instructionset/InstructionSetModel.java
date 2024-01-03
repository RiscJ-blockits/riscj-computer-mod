package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.annotations.SerializedName;
import edu.kit.riscjblockits.model.Value;

import java.util.HashMap;
public class InstructionSetModel implements IQueryableInstructionSetModel {

    private final String name;
    @SerializedName(value = "instruction_length")
    private final int instructionLength;

    @SerializedName(value = "registers")
    private final InstructionSetRegisters instructionSetRegisters;
    @SerializedName(value = "memory")
    private final InstructionSetMemory instructionSetMemory;
    @SerializedName(value = "alu_operations")
    private final String[] aluActions;

    @SerializedName(value = "instructions")
    private final HashMap<String, Instruction> commandHashMap;

    private HashMap<String, Instruction> opcodeHashMap;

    public InstructionSetModel() {
        this.name = null;
        this.instructionLength = 0;
        this.aluActions = null;
        this.commandHashMap = null;
        this.instructionSetMemory = null;
        this.instructionSetRegisters = null;
        this.opcodeHashMap = null;
        this.addressChangeHashMap = null;
        this.programStartLabel = null;
        this.dataStorageKeywords = null;
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

    public void generateOpcodeHashmap(){
        this.opcodeHashMap = new HashMap<>();
        if(this.commandHashMap == null) return;
        this.commandHashMap.values().forEach(e -> opcodeHashMap.put(e.getOpcode(), e));
    }

    public String[] getAluRegisters() {
        return instructionSetRegisters.aluRegs;
    }

    public String getName() {
        return name;
    }

    public Integer getIntegerRegister(String key) {
        return instructionSetRegisters.getIntegerRegister(key);
    }

    public String getProgramCounter() {
        return instructionSetRegisters.getProgramCounter();
    }

    public int getMemoryWordSize() {
        return instructionSetMemory.getWordSize();
    }

    public int getMemoryAddressSize() {
        return instructionSetMemory.getAddressSize();
    }

    public Instruction getInstruction(String s) {
        return commandHashMap.get(s);
    }

    public boolean isAddressChange(String s) {
        return false;
    }

    public String getChangedAddress(String s) {
        return null;
    }

    public String getProgramStartLabel() {
        return programStartLabel;
    }

    public boolean isDataStorageCommand(String s) {
        return false;
    }

    public String getStorageCommandData(String s) {
        return null;
    }

    public int getFetchPhaseLength() {
        return fetchPhase.length;
    }

    public MicroInstruction getFetchPhaseStep(int index) {
        return fetchPhase[index];
    }
}
