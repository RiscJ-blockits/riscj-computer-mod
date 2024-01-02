package edu.kit.riscjblockits.model.blocks;

public interface IViewQueryableBlockModel extends IQueryableBlockModel {
    /**
     * returns whether the state of the block has changed since the last query
     * @return true if the state has changed, false otherwise
     */
    boolean getHasUnqueriedStateChange();
}
