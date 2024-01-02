package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.computerhandler.ClusterHandler;
import edu.kit.riscjblockits.controller.data.IDataContainer;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.BlockPosition;

import java.util.List;

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
