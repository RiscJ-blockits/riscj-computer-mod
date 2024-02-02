package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.IORegisterModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

public class IORegisterController extends RegisterController {

    private boolean isInput;
    private final String type;

    /**
     * Creates a new RegisterController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public IORegisterController(IConnectableComputerBlockEntity blockEntity, boolean isInput, String type) {
        super(blockEntity);
        this.isInput = isInput;
        this.type = type;
        ((IORegisterModel) getModel()).setRegisterType(type);
    }

    @Override
    protected IControllerQueryableBlockModel createBlockModel() {
        return new IORegisterModel(isInput);
    }

    public Value getValue() {
        return ((RegisterModel)getModel()).getValue();
    }

    /**
     * Setter for the value inside the register.
     * @param value The new value that should be stored in the register.
     */
    public void setNewValue(Value value) {
        ((RegisterModel)getModel()).setValue(value);
    }


}
