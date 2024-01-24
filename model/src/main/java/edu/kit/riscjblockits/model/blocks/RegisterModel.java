package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import edu.kit.riscjblockits.model.data.IDataElement;

public class RegisterModel extends BlockModel {

    private int wordLength;

    int i = 0;
    private String registerType;
    private Value value;

    /** ToDo nicht im Entwurf           ToDo Strings hier schlau?
     * Holds the names of the registers that are:
     *                              [0] missing for a valid architecture.
     *                              [1] already present in the cluster.
     */
    private String[] missingAvailableRegisters;

    public RegisterModel() {
        super();
        value = new Value();
        setType(ModelType.REGISTER);
        registerType = "[NOT ASSIGNED]";
        wordLength = 0;
    }

    @Deprecated()
    @Override
    public boolean hasUnqueriedStateChange() {
        return false;
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
        i++;
        value = Value.fromDecimal(Integer.toString(i), 10);


        Data regData = new Data();
        regData.set("type", new DataStringEntry(registerType));
        regData.set("word", new DataStringEntry(String.valueOf(wordLength)));
        regData.set("value", new DataStringEntry(value.toString()));
        Data registersData = new Data();
        registersData.set("missing", new DataStringEntry(missingAvailableRegisters[0]));
        registersData.set("found", new DataStringEntry(missingAvailableRegisters[1]));
        regData.set("registers", registersData);
        return regData;
    }

    public void setWordLength(int wordLength){
        this.wordLength = wordLength;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    /**
     * Setter for the missing and available registers.
     * @param missingAvailableRegisters [0] missing for a valid architecture. [1] already present in the cluster.
     */
    public void setMissingAvailableRegisters(String[] missingAvailableRegisters) {
        this.missingAvailableRegisters = missingAvailableRegisters;
    }

}
