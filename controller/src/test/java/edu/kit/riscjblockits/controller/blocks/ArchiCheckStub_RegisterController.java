package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.clustering.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.RegisterModel;

import java.util.List;

public class ArchiCheckStub_RegisterController implements IQueryableClusterController {
    private final BlockControllerType controllerType;
    private ClusterHandler clusterHandler;
    private final BlockPosition pos;
    private final List<IQueryableClusterController> neighbours;

    private String registerType;

    public ArchiCheckStub_RegisterController(BlockPosition position, String type,
                                             List<IQueryableClusterController> n) {
        registerType = type;
        pos = position;
        controllerType = BlockControllerType.REGISTER;
        neighbours = n;
        new ClusterHandler(this);
        clusterHandler.checkFinished();
    }

    public BlockPosition getBlockPosition() {
        return pos;
    }


    //Only returns bus blocks if the block is a computer block.
    //Returns all computer blocks if the block is a bus block.
    public List<IQueryableClusterController> getNeighbours() {
        return neighbours;
    }

    public ClusterHandler getClusterHandler() {
        return clusterHandler;
    }

    public void setClusterHandler(ClusterHandler clusterHandler) {
        this.clusterHandler = clusterHandler;
    }

    public void onBroken() {
        clusterHandler.blockDestroyed(this);
    }

    @Override
    public BlockControllerType getControllerType() {
        return controllerType;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void neighborUpdate() {}
}
