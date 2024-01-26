package edu.kit.riscjblockits.model.blocks;


public abstract class BlockModel implements IControllerQueryableBlockModel, IViewQueryableBlockModel {

    /**
     * States if this block has a state change that has not been queried by the view yet.
     * Reduces the number of changes that are sent to the view and thus improves performance.
     * (every sent change is a network packet)
     */
    private boolean hasUnqueriedStateChange;
    private ModelType type;
    private BlockPosition position;

    /**
     * Creates a new BlockModel. Should only be called by subclasses.
     */
    protected BlockModel() {
        hasUnqueriedStateChange = true;
    }

    public void setPosition(BlockPosition position) {
        this.position = position;
    }

    public ModelType getType() {
        return type;
    }

    protected void setType(ModelType type) {
        this.type = type;
    }

    public BlockPosition getPosition() {
        return position;
    }

    /**
     * @return whether this block has a state change that has not been queried by the view yet.
     */
    public boolean hasUnqueriedStateChange() {
        return hasUnqueriedStateChange;
    }

    /**
     * @param hasUnqueriedStateChange Sets whether this block has a state change that has not been queried by the view yet.
     */
    protected void setUnqueriedStateChange(boolean hasUnqueriedStateChange) {
       this.hasUnqueriedStateChange = hasUnqueriedStateChange;
    }

}
