package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.computerhandler.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.data.IDataElement;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IQueryableBlockModel;

import java.util.List;

public abstract class ComputerBlockController extends BlockController implements IUserInputReceivableComputerController, IQueryableSimController {

    //private final BlockModel blockModel;
    //ToDo
    private final IControllerQueryableBlockModel blockModel;
    private final IConnectableComputerBlockEntity blockEntity;
    private ClusterHandler clusterHandler;
    private BlockPosition pos;

    private BlockControllerType controllerType;

    protected ComputerBlockController(IConnectableComputerBlockEntity blockEntity) {
        controllerType = BlockControllerType.UNDEFINED;
        pos = blockEntity.getBlockPosition();
        this.blockEntity = blockEntity;
        this.blockModel = createBlockModel();
        //FixMe cast sehr unschön
        blockEntity.setBlockModel((IQueryableBlockModel) this.blockModel);
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

    public void setData(IDataElement data) {
        //
    }

    public String saveAsString() {
        return "";
    }

    public boolean isBus() {
        return false;
    }

    public IQueryableBlockModel getModel() {
        //FixMe cast sehr unschön
        return (IQueryableBlockModel) blockModel;
    }

    public BlockPosition getBlockPosition() {
        return blockEntity.getBlockPosition();
    }

    public List<ComputerBlockController> getNeighbours() {
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
