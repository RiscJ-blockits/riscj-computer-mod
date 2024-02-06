package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_FOUND;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_IO_TIME;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_MISSING;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_TYPE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;

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
     * Defines an RedstoneOutput IO Device.
     */
    public static final String TEXT_OUTPUT = "Terminal";

    /**
     * Whether the register is an input device.
     */
    private final boolean isInput;

    /**
     * Whether the register is an output device.
     */
    private final boolean isOutput;

    /**
     * Creates a new IORegisterModel.
     * @param isInput Whether the register is an input or an output device.
     */
    public IORegisterModel(boolean isInput, boolean isOutput) {
        super();
        this.isInput = isInput;
        this.isOutput = isOutput;
    }

}
