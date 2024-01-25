package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.AluModel;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.data.IDataStringEntry;

import static edu.kit.riscjblockits.model.data.DataConstants.ALU_OPERATION;

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
     *             Data Format: key: "operation", value: "operation"
     *                          key: "operand1", value: operand1 as String
     *                          key: "operand2", value: operand2 as String
     *                          key: "result", value: result as String
     */
    @Override
    public void setData(IDataElement data) {
        if (!data.isContainer()) {
            return;
        }
        for (String s : ((IDataContainer) data).getKeys()) {
            if (s.equals(ALU_OPERATION)) {
                ((AluModel) getModel()).setOperation(((IDataStringEntry)((IDataContainer) data).get(s)).getContent());
            }
            //ToDo sollen wir hier auch die Operanden setzen können?
        }
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
