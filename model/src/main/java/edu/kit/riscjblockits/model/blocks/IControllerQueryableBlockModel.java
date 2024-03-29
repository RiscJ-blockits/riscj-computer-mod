package edu.kit.riscjblockits.model.blocks;

/**
 * Defines all models that can be queried or updated by a controller.
 */
public interface IControllerQueryableBlockModel extends IQueryableBlockModel {

    /**
     * Sets the position of the block.
     * @param position The position of the block in the minecraft world.
     */
    void setPosition(BlockPosition position);

    /**
     * Getter for the position of the block.
     * @return An {@link BlockPosition} with the position of the block.
     */
    BlockPosition getPosition();

    /**
     * Trigger a new synchronization to the client.
     */
    void onStateChange();

    /**
     * Sets the visualization state of the block.
     * @param visualisationState the state to set.
     */
    void setVisualisationState(boolean visualisationState);

}
