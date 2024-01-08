package edu.kit.riscjblockits.model.busgraph;

import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import edu.kit.riscjblockits.model.blocks.BlockPosition;

import java.util.*;

//Vom ClusterHandler zum BusSystem
//Wahrscheinlich auch von der Simulation
//TODO: refactor name, typo
public interface IQueryableBusSystem {

    Value getPresentData();

    boolean getActiveVisualization(BlockPosition blockPosition);

    /**
     * adds a new node to the BusSystemModel
     * @param newBlock is the new node
     */
    void addNode(BlockPosition newBlock);

    /**
     * adds an edge between two nodes
     * @param pos1 is the first node of the edge
     * @param pos2 is the second node of the edge
     */
    void addEdge(BlockPosition pos1, BlockPosition pos2);

    /**
     * removes an edge between two nodes
     * @param pos1 is the first node of the edge
     * @param pos2 is the second node of the edge
     */
    void removeEdge(BlockPosition pos1, BlockPosition pos2);

    /**
     * removes a node and all edges connected to it
     * @param pos1 is the node to remove
     */
    void removeNode(BlockPosition pos1);

    /**
     * returns the graph of the BusSystemModel
     * @return the graph of the BusSystemModel
     */
    Map<BlockPosition, List<BlockPosition>> getBusGraph();

    /**
     * NewNode and OwnNode are already in their busSystems.
     * @param newNode   The recently added node which triggered a combine. Initially added to busSystemToCombine.
     * @param ownNode   The node of the current Bussystem into which is being merged, that is connected to the newNode.
     *                  There can be other connected nodes.
     * @param busSystemToCombine The Bussystem that is merged into the current Bussystem.
     */

    void combineGraph(BlockPosition ownNode, BlockPosition newNode, IQueryableBusSystem busSystemToCombine);

    /**
     * removes a node and all edges connected to it and split the graph when needed to multiple BusSystemModels
     * @param deletedNode is the node to remove
     * @return List of new BusSystemModels
     */
    List<IQueryableBusSystem> splitBusSystemModel(BlockPosition deletedNode);

    /**
     * Only PUBLIC cause of JUnit-Test.
     * Removes one Node and his Edges and split Graph when needed.
     * @param deletedNode The Node to remove.
     * @return List of new Graphs.
     */
    List<Map<BlockPosition, List<BlockPosition>>> splitGraph(BlockPosition deletedNode);

    /**
     * checks if a node is in the BusSystemModel
     * @param blockPosition is the node to check
     * @return true if the node is in the BusSystemModel
     */
    boolean isNode(BlockPosition blockPosition);

}
