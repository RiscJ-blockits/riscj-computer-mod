package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.ControlUnitModel;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;

/**
 * The controller for the control unit block.
 * [JavaDoc in this class partially generated with GitHub Copilot]
 */
public class ControlUnitController extends ComputerBlockController{

    /**
     * Creates a new ControlUnitController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public ControlUnitController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity);
        setControllerType(BlockControllerType.CONTROL_UNIT);
    }

    /**
     * Creates the ControlUnit specific model.
     * @return The model for the ControlUnit.
     */
    @Override
    protected BlockModel createBlockModel() {
        return new ControlUnitModel();
    }

    //ToDo remove
    @Override
    public boolean isBus() {
        return false;
    }

    /**
     * Returns the instruction set model inside the inventory of the block entity.
     * @return An {@link InstructionSetModel} object.
     */
    public InstructionSetModel getInstructionSetModel() {
        return ((ControlUnitModel)getModel()).getIstModel();
    }

    @Override
    public void setData(IDataElement data) {

    }
}
