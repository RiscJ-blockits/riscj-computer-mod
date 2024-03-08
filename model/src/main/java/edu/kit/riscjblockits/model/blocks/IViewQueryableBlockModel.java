package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.IDataElement;

/**
 * Defines the methods the view can use to query the model.
 */
public interface IViewQueryableBlockModel extends IQueryableBlockModel {

    /**
     * Methode to get the visualization state of the block.
     * @return The visualization state of the block.
     */
    boolean getVisualisationState();

    /**
     * Getter for the data saved in the model.
     * @return The data saved in the model. The format depends on the model type.
     */
    IDataElement getData();

    /**
     * Checks if the model has changed since the last query.
     * Returns whether the state of the block has changed since the last query
     * @return true if the state has changed, false otherwise
     */
    boolean hasUnqueriedStateChange();

    /**
     * Resets the state change flag.
     */
    void onStateQuery();

}
