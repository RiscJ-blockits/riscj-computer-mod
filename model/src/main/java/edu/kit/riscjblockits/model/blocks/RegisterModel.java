package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.memoryRepresentation.Value;
import edu.kit.riscjblockits.model.data.IDataElement;

public class RegisterModel extends BlockModel {

    private int wordLength;
    private String registerType;
    private Value value;

    public RegisterModel() {
        value = new Value();
        setType(ModelType.REGISTER);
    }

    @Override
    public boolean hasUnqueriedStateChange() {
        return false;
    }

    @Override
    public IDataElement getData() {
        return null;
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

}
