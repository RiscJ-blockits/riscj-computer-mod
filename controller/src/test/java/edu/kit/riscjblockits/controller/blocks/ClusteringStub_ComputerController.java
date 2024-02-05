package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.clustering.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.BlockPosition;

import java.util.List;

public class ClusteringStub_ComputerController implements IQueryableClusterController {

    private final BlockControllerType controllerType;
    private ClusterHandler clusterHandler;
    private final BlockPosition pos;
    private List<IQueryableClusterController> neighbours;

    public ClusteringStub_ComputerController(BlockPosition position, BlockControllerType type,
                                             List<IQueryableClusterController> n) {
        pos = position;
        if (type == BlockControllerType.BUS) {
            pos.setBus(true);
        }
        controllerType = type;
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

    public void neighborUpdate() {}

    public void setNeighbours(List<IQueryableClusterController> neighbours) {
        this.neighbours = neighbours;
    }

    public void addNeighbour(IQueryableClusterController neighbour) {
        neighbours.add(neighbour);
    }

}
