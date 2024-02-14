package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;


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

    /**
     * Checks if the model has changed since the last query.
     * returns whether the state of the block has changed since the last query
     * @return true if the state has changed, false otherwise
     */
    boolean hasUnqueriedStateChange();

    //ToDo nicht im Entwurfs wiki

    /**
     * Resets the state change flag.
     */
    void onStateQuery();

}
