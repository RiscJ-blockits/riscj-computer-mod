package edu.kit.riscjblockits.model.blocks;


public abstract class BlockModel implements IControllerQueryableBlockModel, IViewQueryableBlockModel {

    private boolean hasUnqueriedStateChange;
    private ModelType type;
    private BlockPosition position;


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
}
