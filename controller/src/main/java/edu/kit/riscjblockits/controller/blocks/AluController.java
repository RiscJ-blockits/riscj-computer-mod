package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.blocks.AluModel;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.IViewQueryableBlockModel;

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
        super(blockEntity);
    }

    /**
     * Creates the Alu specific model.
     * @return The model for the Alu.
     */
    @Override
    protected BlockModel createBlockModel() {
        return new AluModel();
    }

    //ToDo remove
    @Override
    public boolean isBus() {
        return false;
    }

    @Override
    public IViewQueryableBlockModel getModel() {
        return null;
    }

    @Override
    public void setData(IDataElement data) {

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
                break;
        }
    }

}
