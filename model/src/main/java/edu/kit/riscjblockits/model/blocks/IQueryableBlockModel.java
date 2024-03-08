package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;

/**
 * Defines all block models that can be queried.
 */
public interface IQueryableBlockModel {
    /**
     * Getter for the type of the model.
     * @return The type of the model.
     */
    ModelType getType();

    /**
     * Getter for the data saved in the model.
     * @return The data saved in the model. The format depends on the model type.
     */
    IDataElement getData();

}
