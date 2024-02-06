package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.annotations.SerializedName;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Model of an instruction set. Contains all information on how to execute code based on the instruction set.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class
InstructionSetModel implements IQueryableInstructionSetModel {

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
     * Specification of the fetch phase for the instruction set.
     */
    @SerializedName(value = "fetch")
    private final MicroInstruction[] fetchPhase;

    /**
     * Options for changing the address where specific parts of a program will be stored.
     */
    @SerializedName(value = "address_change")
    private final HashMap<String, String> addressChangeHashMap;

    /**
     * Label where the program should be started by default.
     */
    @SerializedName(value = "program_start_label")
    private final String programStartLabel;

    /**
     * Keywords to specify data storage.
     */
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
    @Override
    public String[] getAluRegisters() {
        return instructionSetRegisters.getAluRegs();
    }

    /**
     * Getter for the name of the instruction set.
     * @return Name of the instruction set.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Getter for the integer registers of the instruction set.
     * @param key Key of the register.
     * @return Integer register matching the key.
     */
    @Override
    public Integer getIntegerRegister(String key) {
        // no register object -> no registers
        if (instructionSetRegisters == null) return null;
        // get register Address
        return instructionSetRegisters.getIntegerRegister(key);
    }

    /**
     * Getter for the float registers of the instruction set.
     * @param key Key of the register.
     * @return float register matching the key.
     */
    @Override
    public Integer getFloatRegister(String key) {
        // no register object -> no registers
        if (instructionSetRegisters == null) return null;
        // get register Address
        return instructionSetRegisters.getFloatRegister(key);
    }

    /**
     * Getter for the program counter register of the instruction set.
     * @return Program counter register of the instruction set.
     */
    @Override
    public String getProgramCounter() {
        return instructionSetRegisters.getProgramCounter();
    }

    /**
     * Getter for the word size of the instruction set memory.
     * @return Word size of the instruction set memory.
     */
    @Override
    public int getMemoryWordSize() {
        return instructionSetMemory.getWordSize() / 8 + (instructionSetMemory.getWordSize() % 8 > 0 ? 1 : 0);
    }

    /**
     * Getter for the address size of the instruction set memory.
     * @return Address size of the instruction set memory.
     */
    @Override
    public int getMemoryAddressSize() {
        return instructionSetMemory.getAddressSize() / 8 + (instructionSetMemory.getAddressSize() % 8 > 0 ? 1 : 0);
    }

    /**
     * Getter for an instruction specified by its command keyword.
     * @param s Command keyword of the instruction.
     * @return Instruction matching the command keyword.
     */
    @Override
    public Instruction getInstruction(String s) {
        return commandHashMap.get(s);
    }

    /**
     * Checks if a string is the address change specification of the instruction set.
     * @param s String to check.
     * @return True if the string is the address change specification of the instruction set, false otherwise.
     */
    @Override
    public boolean isAddressChange(String s) {
        for (String key : addressChangeHashMap.keySet()) {
            Pattern p = Pattern.compile(key);
            if (p.matcher(s).matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resolves an address change label to an address.
     * @param s The address change label.
     * @return The address matching the label.
     */
    @Override
    public String getChangedAddress(String s) {
        for (String key : addressChangeHashMap.keySet()) {
            Pattern p = Pattern.compile(key);
            Matcher m = p.matcher(s);
            if (m.matches()) {
                // check if address change needs dynamic replacing
                Pattern groupPattern = Pattern.compile("\\[(?<name>\\w+)]");
                Matcher groupMatcher = groupPattern.matcher(addressChangeHashMap.get(key));
                if (groupMatcher.matches()) {
                    String groupName = groupMatcher.group("name");
                    return m.group(groupName);
                }

                // return constant address otherwise
                return addressChangeHashMap.get(key);
            }
        }
        return null;
    }

    /**
     * Getter for the program start label.
     * @return The program start label.
     */
    @Override
    public String getProgramStartLabel() {
        return programStartLabel;
    }

    /**
     * Checks if a string is a data storage command.
     * @param s The string to check.
     * @return True if the string is a data storage command, false otherwise.
     */
    @Override
    public boolean isDataStorageCommand(String s) {
        for (String key : dataStorageKeywords.keySet()) {
            Pattern p = Pattern.compile(key);
            if (p.matcher(s).matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resolves a data storage command to the data that shall be stored in the needed format.
     * @param s The data storage command.
     * @return The data to be stored.
     */
    @Override
    public String getStorageCommandData(String s) {
        for (String key : dataStorageKeywords.keySet()) {
            Pattern p = Pattern.compile(key);
            Matcher m = p.matcher(s);
            if (m.matches()) {
                // check if data needs dynamic replacing
                Pattern groupPattern = Pattern.compile("\\[(?<name>\\w+)]<(?<length>\\d+)>");
                Matcher groupMatcher = groupPattern.matcher(dataStorageKeywords.get(key));
                if (groupMatcher.matches()) {
                    String groupName = groupMatcher.group("name");
                    return m.group(groupName) + "~" + groupMatcher.group("length");
                }

                // return constant data otherwise
                return dataStorageKeywords.get(key);
            }
        }
        return null;
    }

    /**
     * Getter for the fetch phase length of the instruction set.
     * @return Fetch phase length of the instruction set.
     */
    @Override
    public int getFetchPhaseLength() {
        return fetchPhase.length;
    }

    /**
     * Getter for a microinstruction of the fetch phase of the instruction set.
     * @param index Index of the microinstruction in the fetch phase.
     * @return The microinstruction at the specified index.
     */
    @Override
    public MicroInstruction getFetchPhaseStep(int index) {
        return fetchPhase[index];
    }

    @Override
    public IQueryableInstruction getInstructionFromBinary(String binaryValue) {

        for (int opCodeLength : instructionSetMemory.getPossibleOpcodeLengths()) {
            int opCodeStart;
            if (Objects.equals(instructionSetMemory.getOpcodePosition(), "MOST")) {
                opCodeStart = 0;
            } else if (Objects.equals(instructionSetMemory.getOpcodePosition(), "LEAST")) {
                opCodeStart = instructionSetMemory.getWordSize() - opCodeLength;
            } else {
                opCodeStart = 0;
            }
            String opCode = binaryValue.substring(opCodeStart, opCodeStart + opCodeLength);
            // if no instruction is found, the next opcode length is tried
            if (!opcodeHashMap.containsKey(opCode)) continue;
            Instruction instruction = opcodeHashMap.get(opCode);
            if (instruction != null) {
                return new Instruction(instruction, binaryValue);
            }
        }
        return null;
    }

    /**
     * ToDo nicht im Entwurf
     * @return Returns the names of all registers.
     */
    public List<String> getRegisterNames() {
        return instructionSetRegisters.getRegisterNames();
    }

    public String getRegisterInitialValue(String key) {
        return instructionSetRegisters.getInitialValue(key);
    }

    /**
     * Get the Instructions offered by the instruction set to present in the view.
     * @return List of instructions.
     */
    public ArrayList<String[]> getPossibleInstructions() {
        ArrayList<String[]> instructionList = new ArrayList<>();
        if(commandHashMap == null) return instructionList;
        for(String instructionKey : commandHashMap.keySet()) {
            instructionList.add(new String[]{instructionKey,
                    Arrays.stream(commandHashMap.get(instructionKey).getArguments()).reduce("", (a, b) -> a + ", " + b)});
        }

        return instructionList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InstructionSetModel that = (InstructionSetModel) o;

        if (instructionLength != that.instructionLength) return false;
        assert name != null;
        if (!name.equals(that.name)) return false;
        assert programStartLabel != null;
        if (!programStartLabel.equals(that.programStartLabel)) return false;
        //ToDo finish
        return true;
    }

}
