package edu.kit.riscjblockits.model.busgraph;

import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;

/**
 *
 */
public interface IBusSystem {

    /**
     * calculates shortest path between two nodes avoiding Non-Bus-Nodes and setting the presentData
     * @param startPos is the start node
     * @param endPos is the end node
     * @param presentData is the data that is present on the bus
     */
    public void setBusDataPath(BlockPosition startPos, BlockPosition endPos, Value presentData);

}
