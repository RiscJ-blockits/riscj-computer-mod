package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.computerhandler.ClusterHandler;
import edu.kit.riscjblockits.controller.data.IDataContainer;
import edu.kit.riscjblockits.model.blocks.BlockModel;
import edu.kit.riscjblockits.model.blocks.BlockPosition;

import java.util.List;

/** ToDo besser schreiben
 * Every entity from the mod has a BlockController that is responsible for handling user input and handling the model.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public abstract class BlockController {

    /**
     * The model of the block. Every controller stores a model to keep the data of the block.
     */
    private final BlockModel blockModel;

    /**
     * The block entity that the controller is responsible for.
     */
    private final IQueryableBlockEntity blockEntity;

    /**
     * The cluster handler that is responsible for the cluster that the block is in.
     * One cluster represents multiple computer blocks that are connected by bus blocks.
     * They form one computer.
     */
    private ClusterHandler clusterHandler;

    /**
     * The position of the block in the minecraft world.
     */
    private BlockPosition pos;

    /**
     * The type of the controller. Used to determine the type of the controller.
     */
    private BlockControllerType controllerType;

    /**
     * Creates a new BlockController.
     * Also begins the simulation if the computer is completely built.
     * @param blockEntity The block entity that the controller is responsible for.
     */
    protected BlockController(IQueryableBlockEntity blockEntity) {
        controllerType = BlockControllerType.UNDEFINED;
        pos = blockEntity.getBlockPosition();
        this.blockEntity = blockEntity;
        this.blockModel = createBlockModel();
        blockEntity.setBlockModel(this.blockModel);
        pos = blockEntity.getBlockPosition();
        blockModel.setPosition(pos);
        //start clustering
        new ClusterHandler(this);
        clusterHandler.checkFinished();     //check if the computer ist complete, starts simulation if it is.
    }

    /**
     * Creates the model for the block.
     * Every type of block has its own type of model.
     * @return A block-specific model.
     */
    abstract protected BlockModel createBlockModel();

    /**
     * ToDo
     * @param data
     */
    public void manipulateModelData(byte[] data) {
        //blockModel.setData(data);
    }

    /**
     * ToDo
     * @param data
     */
    public void setData(IDataContainer data) {
        //
    }

    /**
     * ToDo
     * @return
     */
    public String saveAsString() {
        return "";
    }

    /**
     * ToDo remove this method, replace with getControllerType()
     * @return
     */
    public boolean isBus() {
        return false;
    }

    /** ToDo ?? angeblich hab ich das geschrieben, aber ich weiß nicht warum
     * Nur für den Bus relevant
     * @return NULL wenn kein Bus, Model wenn ein Bus.
     */
    public Object getModel() {
        return blockModel;
    }

    /**
     * Getter for the position of the block in the minecraft world.
     * @return An {@link BlockPosition} with the position of the block.
     */
    public BlockPosition getBlockPosition() {
        return pos;
    }

    /**
     * Gather the neighbors of the block.
     * Only returns bus blocks if the block is a computer block.
     * Returns all computer blocks if the block is a bus block.
     * Returns an empty list if the block is not a computer block. ToDo not implemented yet.
     * @return List of neighbors.
     */
    public List<BlockController> getNeighbours() {
        return blockEntity.getComputerNeighbours();
    }

    /**
     * @return The associated ClusterHandler.
     */
    public ClusterHandler getClusterHandler() {
        return clusterHandler;
    }

    /**
     * Sets the associated ClusterHandler.
     * @param clusterHandler The ClusterHandler that should be associated with the controller.
     */
    public void setClusterHandler(ClusterHandler clusterHandler) {
        this.clusterHandler = clusterHandler;
    }

    /**
     * Called by the entity when the block is broken.
     * Removes the block from the cluster.
     */
    public void onBroken() {
        System.out.println("Block broken");
        clusterHandler.blockDestroyed(this);
    }

    /**
     * @return The type of the controller.
     */
    public BlockControllerType getControllerType() {
        return controllerType;
    }

    /**
     * Sets the type of the controller.
     * @param controllerType The type of the controller.
     */
    public void setControllerType(BlockControllerType controllerType) {
        this.controllerType = controllerType;
    }

    /**
     * Gets called every tick by the entity.
     * Overwritten by the controller of the system clock.
     */
    public void tick() {
        //does nothing in most cases, needs to be overwritten if something should happen.
    }

}
