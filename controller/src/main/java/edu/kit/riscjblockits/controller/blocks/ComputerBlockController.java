package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.clustering.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IControllerQueryableBlockModel;
import edu.kit.riscjblockits.model.blocks.IViewQueryableBlockModel;
import edu.kit.riscjblockits.model.data.IDataElement;

import java.util.List;

/**
 * Defines all Computer Controllers that take part in the simulation.
 * Every Computer block Entity has a ComputerBlockController.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public abstract class ComputerBlockController extends BlockController
        implements IUserInputReceivableComputerController, IQueryableClusterController, IQueryableSimController {

    /**
     * The model of the block. Every computer controller stores a model to keep the data of the block.
     */
    private final IControllerQueryableBlockModel blockModel;

    /**
     * The block entity that the controller is responsible for.
     */
    private final IConnectableComputerBlockEntity blockEntity;

    /**
     * The cluster handler that is responsible for the cluster that the block is in.
     * One cluster represents multiple computer blocks that are connected by bus blocks.
     * They form one computer.
     */
    private ClusterHandler clusterHandler;

    /**
     * Creates a new BlockController.
     * Also begins the simulation if the computer is completely built.
     * @param blockEntity The block entity that the controller is responsible for.
     * @param controllerType The type of the controller.
     */
    protected ComputerBlockController(IConnectableComputerBlockEntity blockEntity, BlockControllerType controllerType) {
        super(controllerType);
        this.blockEntity = blockEntity;
        this.blockModel = createBlockModel();
        blockEntity.setBlockModel((IViewQueryableBlockModel) this.blockModel);
    }

    /**
     * Starts the clustering process.
     * @param pos The position of the block in the minecraft world.
     */
    public void startClustering(BlockPosition pos) {
        blockModel.setPosition(pos);
        //Create a new Cluster and check if it is complete. This check can trigger a simulation start.
        new ClusterHandler(this);
        blockModel.onStateChange();
        clusterHandler.checkFinished();
    }

    /**
     * Creates the model for the block.
     * Every type of block has its own type of model.
     * @return A block-specific model.
     */
    protected abstract IControllerQueryableBlockModel createBlockModel();

    /**
     * Used from the view if it wants to update Data in the model.
     * @param data The data that should be set.
     */
    @Override
    public void setData(IDataElement data) {
        blockModel.onStateChange();
    }

    /**
     * Used by the Controller to get the Model of other controllers.
     * @return An {@link IControllerQueryableBlockModel} object.
     */
    public IControllerQueryableBlockModel getModel() {
        return blockModel;
    }

    /**
     * Getter for the block entity.
     * @return The block entity that the controller is responsible for.
     */
    protected IConnectableComputerBlockEntity getBlockEntity() {
        return blockEntity;
    }

    /**
     * Getter for the position of the block in the minecraft world.
     * @return An {@link BlockPosition} with the position of the block.
     */

    public BlockPosition getBlockPosition() {
        return blockModel.getPosition();
    }

    /**
     * Gather the neighbors of the block.
     * Only return bus blocks if the block is a computer block.
     * Returns all computer blocks if the block is a bus block.
     * Returns an empty list if the block is not a computer block.
     * @return List of neighbors.
     */
    public List<IQueryableClusterController> getNeighbours() {
        return blockEntity.getComputerNeighbours().stream().map(IQueryableClusterController.class::cast).toList();
    }

    /**
     * Getter for the associated ClusterHandler.
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
        clusterHandler.blockDestroyed(this);
    }

    /**
     * Gets called every tick by the entity.
     * Overwritten by the controller of the system clock.
     */
    public void tick() {}

    /**
     * Activates the visualization of the block.
     */
    public void activateVisualisation() {
        blockModel.setVisualisationState(true);
    }

    /**
     * Deactivates the visualization of the block.
     */
    public void stopVisualisation() {
        blockModel.setVisualisationState(false);
    }

    /**
     * Creates a new effect at position of the block entity.
     * @param effect The effect that should be spawned.
     */
    protected void spawnEffect(IConnectableComputerBlockEntity.ComputerEffect effect) {
        blockEntity.spawnEffect(effect);
    }

}
