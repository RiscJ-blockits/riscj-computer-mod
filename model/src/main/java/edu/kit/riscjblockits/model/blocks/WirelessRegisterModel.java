package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;


public class WirelessRegisterModel extends RegisterModel {

    /**
     * The register model that holds the data of the register.
     */
    private RegisterModel registerModel;

    public WirelessRegisterModel() {
        super();
        registerModel = new RegisterModel();
    }

    /**
     * Sets the register model.
     * @param registerModel The register model that holds the data of the register.
     */
    public void setRegisterModel(RegisterModel registerModel) {
        System.out.println("New Model set");
        this.registerModel = registerModel;
    }

    /**
     * Getter for the register model.
     * @return The register model that holds the data of the register.
     */
    public RegisterModel getRegisterModel() {
        return registerModel;
    }

    /**
     * Sets the data of the register.
     * @param value The data of the register.
     */
    public void setValue(Value value) {
        registerModel.setValue(value);
    }

    /**
     * Sets the word length of the register.
     * @return The word length of the register.
     */
    public IDataElement getData() {
        return registerModel.getData();
    }

    /**
     * Getter for the data of the register.
     * @return The data of the register.
     */
    public Value getValue() {
        return registerModel.getValue();
    }

    /**
     * Getter for the word length of the register.
     * @param wordLength The word length of the register.
     */
    public void setWordLength(int wordLength) {
        registerModel.setWordLength(wordLength);
    }

    /**
     * Getter for the register type.
     * @return The register type.
     */
    public String getRegisterType() {
        return registerModel.getRegisterType();
    }

    /**
     * Sets the register type.
     * @param registerType The register type.
     */
    public void setRegisterType(String registerType) {
        registerModel.setRegisterType(registerType);
    }

    /**
     * Getter for the missing available registers.
     * @param missingAvailableRegisters [0] missing for a valid architecture. [1] already present in the cluster.
     */
    public void setMissingAvailableRegisters(String[] missingAvailableRegisters) {
        registerModel.setMissingAvailableRegisters(missingAvailableRegisters);
    }
}
