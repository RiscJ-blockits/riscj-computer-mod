package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;

public abstract class BlockController {

    private BlockControllerType controllerType;

    protected BlockController() {
        controllerType = BlockControllerType.UNDEFINED;

    }

    public void setData(IDataElement data) {
        //
    }

    public BlockControllerType getControllerType() {
        return controllerType;
    }

    public void setControllerType(BlockControllerType controllerType) {
        this.controllerType = controllerType;
    }

}
