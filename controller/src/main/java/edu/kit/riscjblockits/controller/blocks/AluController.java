package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.blocks.AluModel;

/**
 * The controller for an ALU block entity.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class AluController extends ComputerBlockController {

    /**
     * Creates a new AluController.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    public AluController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity, BlockControllerType.ALU);
    }

    /**
     * Creates the Alu specific model.
     * @return The model for the Alu.
     */
    @Override
    protected IControllerQueryableBlockModel createBlockModel() {
        return new AluModel();
    }

    /**
     * Used from the view if it wants to update Data in the model.
     * @param data The data that should be set.
     */
    @Override
    public void setData(IDataElement data) {
        //ToDo
        ((AluModel) getModel()).setOperation(null);
    }

    /**
     * Executes the alu operation by accessing the model and setting all relevant values
     * @param operation Alu operation to execute
     */
    public void executeAluOperation(String operation) {
        //ToDo
        switch (operation) {
            case "ADD":
                //ToDo
                ((AluModel) getModel()).setResult(null);
                break;
        }
    }

}
