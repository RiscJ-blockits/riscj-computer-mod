package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.IViewQueryableBlockModel;
import edu.kit.riscjblockits.model.data.IDataElement;

import java.util.List;

/**
 * Defines all Computer Entities that can be queried or updated by a controller.
 */
public interface IConnectableComputerBlockEntity {

    /**
     * @param model Sets the model of the block entity, so it can query the model for visualization.
     */
    void setBlockModel(IViewQueryableBlockModel model);

    /**
     * Gather the neighbors of the block.
     * Only returns bus blocks if the block is a computer block.
     * Returns all computer blocks if the block is a bus block.
     * Returns an empty list if the block is not a computer block.
     * @return List of neighbors.
     */
    List<ComputerBlockController> getComputerNeighbours();

    /**
     * @return The position of the block in the minecraft world.
     */
    BlockPosition getBlockPosition();

    /**
     * The Controllers can get initial Data from NBT.
     * @return
     */
    IDataElement getBlockEntityData();

    void neighborUpdate();

    void spawnEffect(ComputerEffect effect);

    enum ComputerEffect {
        EXPLODE,
        SMOKE,
    }

}
