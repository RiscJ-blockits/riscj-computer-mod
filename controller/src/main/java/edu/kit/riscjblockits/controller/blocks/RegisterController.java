package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.Value;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.IDataElement;

public class RegisterController extends ComputerBlockController {
    public RegisterController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity);
        setControllerType(BlockControllerType.REGISTER);
    }

    @Override
    protected BlockModel createBlockModel() {
        return new RegisterModel();
    }

    public String getRegisterType() {
        return ((RegisterModel)getModel()).getRegisterType();
    }

    public Value getValue() {
        return ((RegisterModel)getModel()).getValue();
    }

    public void setNewValue(Value value) {
        //ToDo check input
        ((RegisterModel)getModel()).setValue(value);
    }


    @Override
    public void setData(IDataElement data) {

    }
}
