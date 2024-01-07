package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.clustering.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.BlockPosition;

import java.util.List;

public interface IQueryableClusterController extends IQueryableComputerController {

    /**
     * Sets the associated ClusterHandler.
     * @param clusterHandler The ClusterHandler that should be associated with the controller.
     */
    void setClusterHandler(ClusterHandler clusterHandler);

    /**
     * @return The associated ClusterHandler.
     */

    ClusterHandler getClusterHandler();

    /**
     * Gather the neighbors of the block.
     * Only returns bus blocks if the block is a computer block.
     * Returns all computer blocks if the block is a bus block.
     * Returns an empty list if the block is not a computer block. ToDo not implemented yet.
     * @return List of neighbors.
     */
    List<ComputerBlockController> getNeighbours();

    /**
     * Getter for the position of the block in the minecraft world.
     * @return An {@link BlockPosition} with the position of the block.
     */

    BlockPosition getBlockPosition();

}
