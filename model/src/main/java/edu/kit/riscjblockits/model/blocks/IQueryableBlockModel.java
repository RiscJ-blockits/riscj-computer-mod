package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;


public interface IQueryableBlockModel {
    ModelType getType();

    /**
     * @return The data saved in the model. The format depends on the model type.
     */
    IDataElement getData();

    /**
     * returns whether the state of the block has changed since the last query
     * @return true if the state has changed, false otherwise
     */
    boolean hasUnqueriedStateChange();

}
