package edu.kit.riscjblockits.model.blocks;

/**
 * Defines the methods the view can use to query the model.
 */
public interface IViewQueryableBlockModel extends IQueryableBlockModel {

    //ToDo nicht im Entwurfs wiki
    /**
     * Methode to get the visualisation state of the block.
     * @return The visualisation state of the block.
     */
    boolean getVisualisationState();
}
