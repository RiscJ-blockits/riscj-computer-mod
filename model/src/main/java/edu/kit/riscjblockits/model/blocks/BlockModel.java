package edu.kit.riscjblockits.model.blocks;

public abstract class BlockModel implements IControllerQueryableBlockModel, IViewQueryableBlockModel {

    private boolean hasUnqueriedStateChange;
    private byte[] data;
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
}
