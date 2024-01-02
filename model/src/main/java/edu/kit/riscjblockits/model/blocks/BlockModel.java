package edu.kit.riscjblockits.model.blocks;

public abstract class BlockModel implements IControllerQueryableBlockModel, IViewQueryableBlockModel {

    private boolean hasUnqueriedStateChange;
    private byte[] data;
    private ModelType type;
    private BlockPosition position;

    //Public Access Methods distinguished by users delegating access logic


    //Private methods for access logic
    private byte[] getData() {
        return data;
    }

    private void setData(byte[] data) {
        this.data = data;
    }

    public void setPosition(BlockPosition position) {
        this.position = position;
    }

    public ModelType getType() {
        return type;
    }

    public void setType(ModelType type) {
        this.type = type;
    }
}
