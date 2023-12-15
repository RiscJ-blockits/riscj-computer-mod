package edu.kit.riscjblockits.model.blocks;

public class MemoryModel extends BlockModel {
    @Override
    public boolean getHasUnqueriedStateChange() {
        return false;
    }
}
