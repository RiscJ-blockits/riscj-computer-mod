package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IViewQueryableBlockModel;

import java.util.List;

/**
 * Defines all Computer Entities that can be queried or updated by a controller.
 */
public interface IConnectableComputerBlockEntity {

    /**
     * Sets the model of the block entity, so it can query the model for visualization.
     * @param model Sets the model of the block entity, so it can query the model for visualization.
     */
    void setBlockModel(IViewQueryableBlockModel model);

    /**
     * Gather the neighbors of the block.
     * Only return bus blocks if the block is a computer block.
     * Returns all computer blocks if the block is a bus block.
     * Returns an empty list if the block is not a computer block.
     * @return List of neighbors.
     */
    List<ComputerBlockController> getComputerNeighbours();

    /**
     * Returns the position of the block in the minecraft world.
     * @return The position of the block in the minecraft world.
     */
    BlockPosition getBlockPosition();

    /**
     * Spawns an effect at the position of the block entity.
     * @param effect The effect that should be spawned.
     */
    void spawnEffect(ComputerEffect effect);

    /**
     * Defines the possible effects that can be spawned in the minecraft world by a block controller.
     */
    enum ComputerEffect {
        /**
         * The block explodes.
         */
        EXPLODE,
        /**
         * The block smokes.
         */
        SMOKE
    }
}
