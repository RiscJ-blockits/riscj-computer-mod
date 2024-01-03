package edu.kit.riscjblockits.model.blocks;

/**
 * Defines all models that can be queried or updated by a controller.
 */
public interface IControllerQueryableBlockModel {

    /**
     * Sets the position of the block.
     * @param position The position of the block in the minecraft world.
     */
    void setPosition(BlockPosition position);


}
