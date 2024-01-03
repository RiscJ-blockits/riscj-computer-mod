package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.annotations.SerializedName;
import edu.kit.riscjblockits.model.Value;

import java.util.HashMap;

/**
 * Model of an instruction set. Contains all information on how to execute code based on the instruction set.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class InstructionSetModel implements IQueryableInstructionSetModel {

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

    @SerializedName(value = "fetch")
    private final MicroInstruction[] fetchPhase;

    @SerializedName(value = "address_change")
    private final HashMap<String, String> addressChangeHashMap;

    @SerializedName(value = "program_start_label")
    private final String programStartLabel;

    @SerializedName(value = "data_storage_keywords")
    private final HashMap<String, String> dataStorageKeywords;

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
        this.fetchPhase = null;
        this.commandHashMap = null;
        this.instructionSetMemory = null;
        this.instructionSetRegisters = null;
        this.opcodeHashMap = null;
        this.addressChangeHashMap = null;
        this.programStartLabel = null;
        this.dataStorageKeywords = null;
    }

    public InstructionSetModel(String name, int instructionLength, InstructionSetRegisters instructionSetRegisters,
                               InstructionSetMemory instructionSetMemory, String[] aluActions,
                               MicroInstruction[] fetchPhase, HashMap<String, String> addressChangeHashMap,
                               String programStartLabel, HashMap<String, String> dataStorageKeywords,
                               HashMap<String, Instruction> commandHashMap,
                               HashMap<String, Instruction> opcodeHashMap) {
        this.name = name;
        this.instructionLength = instructionLength;
        this.instructionSetRegisters = instructionSetRegisters;
        this.instructionSetMemory = instructionSetMemory;
        this.aluActions = aluActions;
        this.fetchPhase = fetchPhase;
        this.addressChangeHashMap = addressChangeHashMap;
        this.programStartLabel = programStartLabel;
        this.dataStorageKeywords = dataStorageKeywords;
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
