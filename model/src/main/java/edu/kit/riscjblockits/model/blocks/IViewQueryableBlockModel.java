package edu.kit.riscjblockits.model.blocks;

/**
 * Defines the methods the view can use to query the model.
 */
public interface IViewQueryableBlockModel extends IQueryableBlockModel {

    /**
     * Methode to get the visualization state of the block.
     * @return The visualization state of the block.
     */
    boolean getVisualisationState();
}
