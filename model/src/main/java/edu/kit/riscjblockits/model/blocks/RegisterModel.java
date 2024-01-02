package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.Value;

public class RegisterModel extends BlockModel {

    private int wordLength;
    private String registerType;
    private Value value;

    public RegisterModel() {
        value = new Value();
    }

    @Override
    public boolean getHasUnqueriedStateChange() {
        return false;
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
