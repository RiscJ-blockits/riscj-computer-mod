package edu.kit.riscjblockits.model.blocks;

public class IORegisterModel extends RegisterModel {

    /**
     * Defines an RedstoneOutput IO Device.
     */
    public static final String REDSTONE_OUTPUT = "RedstoneOutput";

    /**
     * Defines an RedstoneInput IO Device.
     */
    public static final String REDSTONE_INPUT = "RedstoneInput";

    /**
     * Whether the register is an input or an output device.
     */
    private boolean isInput;

    /**
     * Creates a new IORegisterModel.
     * @param isInput Whether the register is an input or an output device.
     */
    public IORegisterModel(boolean isInput) {
        super();
        this.isInput = isInput;
    }

}
