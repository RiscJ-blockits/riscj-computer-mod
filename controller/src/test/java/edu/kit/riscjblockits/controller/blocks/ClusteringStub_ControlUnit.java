package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.clustering.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IViewQueryableBlockModel;
import edu.kit.riscjblockits.model.data.IDataElement;

import java.util.List;

public class ClusteringStub_ControlUnit extends ControlUnitController implements IQueryableClusterController {

    private final BlockControllerType controllerType;
    private ClusterHandler clusterHandler;
    private final BlockPosition pos;
    private List<IQueryableClusterController> neighbours;

    public ClusteringStub_ControlUnit(BlockPosition position, BlockControllerType type, List<IQueryableClusterController> n) {
        super(new IConnectableComputerBlockEntity() {
            @Override
            public void setBlockModel(IViewQueryableBlockModel model) {}

            @Override
            public List<ComputerBlockController> getComputerNeighbours() {
                return null;
            }

            @Override
            public BlockPosition getBlockPosition() {
                return null;
            }

            @Override
            public void spawnEffect(ComputerEffect effect) {

            }
        });
        pos = position;
        controllerType = type;
        neighbours = n;
        new ClusterHandler(this);
        clusterHandler.checkFinished();
    }

    @Override
    public void rejectIstModel() {}

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

    @Override
    public void stopVisualisation() {
        //
    }

    @Override
    public void setData(IDataElement data) {}
}
