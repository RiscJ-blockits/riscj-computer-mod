package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.blocks.AluModel;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.IViewQueryableBlockModel;

public class AluController extends ComputerBlockController {
    public AluController(IConnectableComputerBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    protected BlockModel createBlockModel() {
        return new AluModel();
    }

    //@Override
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
