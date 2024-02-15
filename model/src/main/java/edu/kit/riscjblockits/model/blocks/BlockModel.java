package edu.kit.riscjblockits.model.blocks;

/**
 * Represents one block.
 * Holds the data needed for the view and the controller.
 * Every computer block has a model.
 */
public abstract class BlockModel implements IControllerQueryableBlockModel, IViewQueryableBlockModel {

    /**
     * States if this block has a state change that has not been queried by the view yet.
     * Reduces the number of changes that are sent to the view and thus improves performance.
     * (every sent change is a network packet)
     */
    private boolean hasUnqueriedStateChange;
    /**
     * States if the block is currently working and should be active in the view.
     */
    private boolean visualisationState;

    /**
     * Type of the block. Used to distinguish between different models.
     */
    private ModelType type;

    /**
     * Position of the block that holds this model in the minecraft world.
     */
    private BlockPosition position;

    /**
     * Creates a new BlockModel. Should only be called by subclasses.
     */
    protected BlockModel() {
        hasUnqueriedStateChange = true;
        visualisationState = false;
    }

    /**
     * Sets the position of the associated block in the minecraft world.
     * @param position The position of the associated block in the minecraft world.
     */
    public void setPosition(BlockPosition position) {
        this.position = position;
    }

    /**
     * Returns the type of the block. Used to distinguish between different model types.
     * @return The type of the block
     */
    public ModelType getType() {
        return type;
    }

    /**
     * Sets the type of the block. Used to distinguish between different model types.
     * @param type The type of the block
     */
    protected void setType(ModelType type) {
        this.type = type;
    }

    /**
     * Returns the position of the associated block in the minecraft world.
     * @return The position of the associated block in the minecraft world.
     */
    public BlockPosition getPosition() {
        return position;
    }

    /**
     * Checker for the state of the data whether it has changed and has not been queried by the view yet.
     * @return whether this block has a state change that has not been queried by the view yet.
     */
    public boolean hasUnqueriedStateChange() {
        return hasUnqueriedStateChange;
    }

    /**
     * Sets whether this block has a state change that has not been queried by the view yet.
     * @param hasUnqueriedStateChange true if this block has a state change that has not been queried by the view yet.
     */
    protected void setUnqueriedStateChange(boolean hasUnqueriedStateChange) {
        this.hasUnqueriedStateChange = hasUnqueriedStateChange;
    }

    /**
     * Notifies the model that the view has queried its state.
     */
    public void onStateQuery() {
        hasUnqueriedStateChange = false;
    }

    /**
     * Notifies the model that its state has changed.
     */
    public void onStateChange() {
        hasUnqueriedStateChange = true;
    }

    public boolean getVisualisationState() {
        return visualisationState;
    }

    public void setVisualisationState(boolean visualisationState) {
        this.visualisationState = visualisationState;
    }

}
