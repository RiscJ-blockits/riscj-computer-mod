package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.ControlUnitModel;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
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
        super(blockEntity, BlockControllerType.CONTROL_UNIT);
    }

    /**
     * Creates the ControlUnit specific model.
     * @return The model for the ControlUnit.
     */
    @Override
    protected IControllerQueryableBlockModel createBlockModel() {
        return new ControlUnitModel();
    }

    /**
     * Returns the instruction set model inside the inventory of the block entity.
     * @return An {@link InstructionSetModel} object.
     */
    public IQueryableInstructionSetModel getInstructionSetModel() {
        return ((ControlUnitModel)getModel()).getIstModel();
    }

    /**
     * Used from the view if it wants to update Data in the model.
     * @param data The data that should be set.
     */
    @Override
    public void setData(IDataElement data) {
        /* Data Format: key: "clustering", value: container
         *                                  key: "missingRegisters", value: string space-separated register names
         *                                  key: "foundRegisters", value: string space-separated register names
         *                                  key: "foundMemory", value: string with number of memory blocks
         *                                  key: "foundAlu", value: string with number of alu blocks
         *                                  key: "foundControlUnit", value: string with number of control unit blocks
         *                                  key: "foundControlUnit", value: string with number of control unit blocks
         */
        if (!data.isContainer()) {
            return;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals("clustering")) {
                ((ControlUnitModel) getModel()).setClusteringData(((IDataContainer) data).get(s));
            }
            //ToDo add method to update Ist Model
        }

    }

}
