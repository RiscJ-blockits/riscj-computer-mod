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

    protected BlockController(IQueryableBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        this.blockModel = createBlockModel();
        blockEntity.setBlockModel(this.blockModel);
        blockModel.setPosition(getBlockPosition());
        clusterHandler = new ClusterHandler(this);
        clusterHandler.combine();
    }

    abstract protected BlockModel createBlockModel();

    private List<ClusterHandler> controllerListToClusterList(List<BlockController> blockControllers) {
        return null;
    }

    public void manipulateModelData(byte[] data) {
        //blockModel.setData(data);
    }

    public void setData(IDataContainer data) {
        //
    }

    public String saveAsString() {
        return "";
    }

    //public abstract boolean isBus();

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
}
