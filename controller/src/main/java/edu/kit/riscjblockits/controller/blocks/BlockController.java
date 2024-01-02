package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.computerhandler.ClusterHandler;
import edu.kit.riscjblockits.controller.data.IDataContainer;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.BlockPosition;

import java.util.List;

public abstract class BlockController {

    private final BlockModel blockModel;
    private final IQueryableBlockEntity blockEntity;
    private ClusterHandler clusterHandler;
    private BlockPosition pos;

    private BlockControllerType controllerType;

    protected BlockController(IQueryableBlockEntity blockEntity) {
        controllerType = BlockControllerType.UNDEFINED;
        pos = blockEntity.getBlockPosition();
        this.blockEntity = blockEntity;
        this.blockModel = createBlockModel();
        blockEntity.setBlockModel(this.blockModel);
        blockModel.setPosition(getBlockPosition());
        new ClusterHandler(this);
        clusterHandler.checkFinished();
    }

    abstract protected BlockModel createBlockModel();

    //private List<ClusterHandler> controllerListToClusterList(List<BlockController> blockControllers) {
    //    return null;
    //}

    public void manipulateModelData(byte[] data) {
        //blockModel.setData(data);
    }

    public void setData(IDataContainer data) {
        //
    }

    public String saveAsString() {
        return "";
    }

    public boolean isBus() {
        return false;
    }

    /**
     * Nur für den Bus relevant
     * @return NULL wenn kein Bus, Model wenn ein Bus.
     */
    public Object getModel() {
        return blockModel;
    }

    public BlockPosition getBlockPosition() {
        return blockEntity.getBlockPosition();
    }

    public List<BlockController> getNeighbours() {
        return blockEntity.getComputerNeighbours();
    }

    public ClusterHandler getClusterHandler() {
        return clusterHandler;
    }

    public void setClusterHandler(ClusterHandler clusterHandler) {
        this.clusterHandler = clusterHandler;
    }

    public void onBroken() {
        System.out.println("Block broken");
        clusterHandler.blockDestroyed(this);
    }

    public BlockControllerType getControllerType() {
        return controllerType;
    }

    public void setControllerType(BlockControllerType controllerType) {
        this.controllerType = controllerType;
    }

    /**
     * gets called every tick.
     * Overwritten by Clock
     */
    public void tick() {

    }
}
