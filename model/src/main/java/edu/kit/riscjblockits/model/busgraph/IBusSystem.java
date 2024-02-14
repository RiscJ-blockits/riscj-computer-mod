package edu.kit.riscjblockits.model.busgraph;

import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

/**
 * This interface defines the methods that the bus system provides for the simulation.
 */
public interface IBusSystem {

    /**
     * calculates shortest path between two nodes avoiding Non-Bus-Nodes and setting the presentData.
     * @param startPos is the start node.
     * @param endPos is the end node.
     * @param presentData is the data that is present on the bus.
     */
    void setBusDataPath(BlockPosition startPos, BlockPosition endPos, Value presentData);

    //ToDo nicht im Entwurfs Wiki

    /**
     * resets the visualisation of the bus system.
     */
    void resetVisualisation();

    //ToDo nicht im Entwurfs Wiki

    /**
     * activates the visualisation of the bus system.
     */
    void activateVisualisation();

}
