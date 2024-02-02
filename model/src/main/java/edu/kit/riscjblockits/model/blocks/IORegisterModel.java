package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.memoryrepresentation.Value;

public class IORegisterModel extends RegisterModel {

    public static final String REDSTONE_OUTPUT = "RedstoneOutput";
    public static final String REDSTONE_INPUT = "RedstoneInput";

    private boolean isInput;

    public IORegisterModel(boolean isInput) {
        super();
        this.isInput = isInput;
        //ToDo remove test Code
        setValue(Value.fromHex("08", 6));
    }


}
