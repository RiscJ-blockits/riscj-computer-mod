package edu.kit.riscjblockits.model.blocks;

public abstract class BlockModel implements IControllerQueryable, IViewQueryable {

    private boolean hasUnqueriedStateChange;
    private byte[] data;

    //Public Access Methods distinguished by users delegating access logic


    //Private methods for access logic
    private byte[] getData() {
        return data;
    }

    private void setData(byte[] data) {
        this.data = data;
    }

    public abstract boolean getHasUnqueriedStateChange();
    
}
