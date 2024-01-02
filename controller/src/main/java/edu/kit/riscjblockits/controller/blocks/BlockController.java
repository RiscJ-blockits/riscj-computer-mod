package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.data.IDataContainer;

public abstract class BlockController {

    private BlockControllerType controllerType;

    protected BlockController() {
        controllerType = BlockControllerType.UNDEFINED;

    }

    public void setData(IDataContainer data) {
        //
    }

    public BlockControllerType getControllerType() {
        return controllerType;
    }

    public void setControllerType(BlockControllerType controllerType) {
        this.controllerType = controllerType;
    }

}
