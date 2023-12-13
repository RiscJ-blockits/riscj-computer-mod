package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.computerhandler.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.BlockModel;

import java.util.List;

public abstract class BlockController {

    private final BlockModel blockModel;
    private ClusterHandler clusterHandler;

    protected BlockController(IQueryableBlockEntity blockEntity) {
        this.blockModel = getBlockModel();
        blockEntity.setBlockModel();
        this.clusterHandler = new ClusterHandler(this);
        List<ClusterHandler> neighborClusters = controllerListToClusterList(blockEntity.getComputerNeighbours());
        this.clusterHandler = this.clusterHandler.combine(neighborClusters);
    }

    abstract protected BlockModel getBlockModel();

    private List<ClusterHandler> controllerListToClusterList(List<BlockController> blockControllers) {
        return null;
    }

    public void manipulateModelData(byte[] data) {
        //blockModel.setData(data);
    }

}
