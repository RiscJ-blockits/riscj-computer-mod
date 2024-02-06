package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.IORegisterModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

/**
 * The IORegisterController class is a subclass of RegisterController
 * that represents a register block with input or output functionality.
 */
public class IORegisterController extends RegisterController {

    /**
     * Whether the register is an input or an output device.
     */
    private final boolean isInput;
    private final boolean isOutput;

    /**
     * Creates a new RegisterController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public IORegisterController(IConnectableComputerBlockEntity blockEntity, boolean isInput, boolean isOutput, String type) {
        super(blockEntity);
        this.isInput = isInput;
        this.isOutput = isOutput;
        ((IORegisterModel) getModel()).setRegisterType(type);
    }

    /**
     * Creates a new block model for the IORegisterController.
     * @return The created block model.
     */
    @Override
    protected IControllerQueryableBlockModel createBlockModel() {
        return new IORegisterModel(isInput, isOutput);
    }

    /**
     * @return The value that is stored in the register if it is an input device.
     * Else returns an empty value.
     */
    @Override
    public Value getValue() {
        if (!isInput) return new Value();
        return ((RegisterModel)getModel()).getValue();
    }

    /**
     * @param value The new value that should be stored in the register if it is an output device.
     */
    @Override
    public void setNewValue(Value value) {
        if (!isOutput) return;
        ((RegisterModel)getModel()).setValue(value);
    }

}
