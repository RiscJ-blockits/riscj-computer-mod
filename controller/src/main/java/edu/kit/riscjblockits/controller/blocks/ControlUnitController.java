package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.ControlUnitModel;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;

public class ControlUnitController extends ComputerBlockController{
    public ControlUnitController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity);
        setControllerType(BlockControllerType.CONTROLL_UNIT);
    }

    @Override
    protected BlockModel createBlockModel() {
        return null;
    }

    @Override
    public boolean isBus() {
        return false;
    }

    public InstructionSetModel getInstructionSetModel() {
        return ((ControlUnitModel)getModel()).getIstModel();
    }
}
