package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_FOUND;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_MISSING;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;

/**
 * Represents the data and state of a register. Every register block has one.
 */
public class RegisterModel extends BlockModel {

    /**
     * The default register type. Is displayed in the ui if no register type is set.
     */
    public static final String UNASSIGNED_REGISTER = "[NOT_ASSIGNED]";
    private int wordLength;

    private String registerType;
    private Value value;

    /** ToDo nicht im Entwurf           ToDo Strings hier schlau?
     * Holds the names of the registers that are:
     *                              [0] missing for a valid architecture.
     *                              [1] already present in the cluster.
     */
    private String[] missingAvailableRegisters;

    /**
     * Constructor. Returns the model for a register.
     * Initializes the register type with the default register type.
     * Initializes the value with a zero value.
     */
    public RegisterModel() {
        super();
        value = new Value();
        setType(ModelType.REGISTER);
        registerType = UNASSIGNED_REGISTER;
        wordLength = 3;
    }

    /**
     * Getter for the data the view needs for ui.
     * @return Data Format: key: "type", value: "registerType"
     *                      key: "registers", value: container
     *                                              key: "missing", value: string space-separated register names
     *                                              key: "found", value: string with space-separated register names
     *                      key: "word", value: string with word length
     *                      key: "value", value: string with value
     */
    @Override
    public IDataElement getData() {
        Data regData = new Data();
        regData.set(REGISTER_TYPE, new DataStringEntry(registerType));
        regData.set(REGISTER_WORD_LENGTH, new DataStringEntry(String.valueOf(wordLength)));
        regData.set(REGISTER_VALUE, new DataStringEntry(value.getHexadecimalValue()));
        if (missingAvailableRegisters != null && missingAvailableRegisters.length == 2
                && (missingAvailableRegisters[0] != null) && (missingAvailableRegisters[1] != null)) {
            Data registersData = new Data();
            registersData.set(REGISTER_MISSING, new DataStringEntry(missingAvailableRegisters[0]));
            registersData.set(REGISTER_FOUND, new DataStringEntry(missingAvailableRegisters[1]));
            regData.set(REGISTER_REGISTERS, registersData);
        }
        return regData;
    }

    /**
     * Setter for the register size.
     * Also starts an update of the view.
     * @param wordLength The word length to set.
     */
    public void setWordLength(int wordLength){
        this.wordLength = wordLength;
        setUnqueriedStateChange(true);
    }

    public String getRegisterType() {
        return registerType;
    }

    /**
     * Setter for the register type.
     * Also starts an update of the view.
     * @param registerType The register type to set.
     */
    public void setRegisterType(String registerType) {
        this.registerType = registerType;
        setUnqueriedStateChange(true);
    }

    public Value getValue() {
        return value;
    }

    /**
     * Setter for the value of the register.
     * Also starts an update of the view.
     * @param value The value to set the register to.
     */
    public void setValue(Value value) {
        this.value = value;
        setUnqueriedStateChange(true);
    }

    /**
     * Setter for the missing and available registers.
     * @param missingAvailableRegisters [0] missing for a valid architecture. [1] already present in the cluster.
     */
    public void setMissingAvailableRegisters(String[] missingAvailableRegisters) {
        this.missingAvailableRegisters = missingAvailableRegisters;
        setUnqueriedStateChange(true);
    }

    /**
     * Getter for the word length of the register.
     * @return The word length of the register.
     */
    protected int getWordLength() {
        return wordLength;
    }

    /**
     * Getter for the missing and available registers.
     * @return [0] missing for a valid architecture. [1] already present in the cluster.
     */
    protected String[] getMissingAvailableRegisters() {
        return missingAvailableRegisters;
    }

}
