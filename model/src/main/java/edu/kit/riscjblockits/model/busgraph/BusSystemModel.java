package edu.kit.riscjblockits.model.busgraph;

import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import edu.kit.riscjblockits.model.blocks.BlockPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Holds the BusGraph of a cluster.
 */
public class BusSystemModel implements IQueryableBusSystem, IBusSystem {

    /**
     * Graph of all Blocks in the cluster and their connections.
     */
    private Map<BlockPosition, List<BlockPosition>> adjPositions;

    /**
     * Holds the active nodes in the visualization.
     */
    private Map<BlockPosition, Boolean> activeVisualization;

    /**
     * Holds the data that is present on the bus.
     */
    private Value presentData;

    //TODO: "from", "to" are not used
    private String from;
    private String to;


    /**
     * creates a BusSystemModel with one node
     * @param firstBlock is the first node of the BusSystemModel
     */
    public BusSystemModel(BlockPosition firstBlock) {
        adjPositions = new HashMap<>();
        activeVisualization = new HashMap<>();
        addNode(firstBlock);
    }

    /**
     * creates a BusSystemModel with the given graph
     * @param adjPositions is the graph of the new BusSystemModel
     */
    private BusSystemModel(Map<BlockPosition, List<BlockPosition>> adjPositions) {
        this.adjPositions = adjPositions;
        activeVisualization = new HashMap<>();
    }

    /**
     * calculates shortest path between two nodes avoiding Non-Bus-Nodes and setting the presentData
     * @param startPos is the start node
     * @param endPos is the end node
     * @param presentData is the data that is present on the bus
     */
    public void setBusDataPath(BlockPosition startPos, BlockPosition endPos, Value presentData) {
        this.presentData = presentData;
        //BFS
        List<BlockPosition> discovered = new ArrayList<>();
        Map<BlockPosition, BlockPosition> path = new HashMap<>();
        Queue<BlockPosition> queue = new LinkedList<>();
        queue.add(startPos);
        while (!queue.isEmpty()) {
            BlockPosition current = queue.poll();
            if (current.equals(endPos)) {
                break;
            }
            if (!discovered.contains(current)) {
                discovered.add(current);
                for (BlockPosition neighbor : adjPositions.get(current)) {
                    if (neighbor.isBus() || neighbor.equals(endPos)) {
                        queue.add(neighbor);
                        path.computeIfAbsent(neighbor, k -> current);
                    }
                }
            }
        }
        //backtrack the BFS to find shortest path and set activeVisualization at nodes on the path
        BlockPosition current = endPos;
        while (!current.equals(startPos)) {
            activeVisualization.put(current, true);
            current = path.get(current);
        }
        activeVisualization.put(startPos, true);
    }

    /**
     * returns the present data on the busSystem
     * @return the present data on the busSystem
     */
    public Value getPresentData() {
        return presentData;
    }

    /**
     * returns if the node with the given position is active in the visualization
     * @param blockPosition is the position of the node
     * @return true if the node is active in the visualization
     */
    public boolean getActiveVisualization(BlockPosition blockPosition) {
        boolean active = activeVisualization.get(blockPosition);
        activeVisualization.put(blockPosition, false);
        return active;
    }

    /**
     * adds a new node to the BusSystemModel
     * @param newBlock is the new node
     */
    public void addNode(BlockPosition newBlock) {
        adjPositions.put(newBlock, new ArrayList<>());
        activeVisualization.put(newBlock, false);
    }

    /**
     * adds an edge between two nodes
     * @param pos1 is the first node of the edge
     * @param pos2 is the second node of the edge
     */
    public void addEdge(BlockPosition pos1, BlockPosition pos2) {
        if (isNode(pos1) && isNode(pos2)) {
            adjPositions.get(pos1).add(pos2);
            adjPositions.get(pos2).add(pos1);
        }
    }

    /**
     * removes a node and all edges connected to it
     * @param pos1 is the node to remove
     */
    public void removeNode(BlockPosition pos1) {
        for(BlockPosition pos2: adjPositions.get(pos1)){
            adjPositions.get(pos2).remove(pos1);
        }
        adjPositions.remove(pos1);
    }

    /**
     * returns the graph of the BusSystemModel
     * @return the graph of the BusSystemModel
     */
    public Map<BlockPosition, List<BlockPosition>> getBusGraph() {
        return adjPositions;
    }

    /**
     * NewNode and OwnNode are already in their busSystems.
     * @param newNode   The recently added node which triggered a combine. Initially added to busSystemToCombine.
     * @param ownNode   The node of the current Bussystem into which is being merged, that is connected to the newNode.
     *                  There can be other connected nodes.
     * @param busSystemToCombine The Bussystem that is merged into the current Bussystem.
     */

    public void combineGraph(BlockPosition ownNode, BlockPosition newNode, IQueryableBusSystem busSystemToCombine) {
        if (!busSystemToCombine.equals(this)) {
            adjPositions.putAll(busSystemToCombine.getBusGraph());
        }
        addEdge(newNode, ownNode);
    }

    /**
     * removes a node and all edges connected to it and split the graph when needed to multiple BusSystemModels
     * @param deletedNode is the node to remove
     * @return List of new BusSystemModels
     */
    public List<IQueryableBusSystem> splitBusSystemModel(BlockPosition deletedNode) {
        List<Map<BlockPosition, List<BlockPosition>>> newGraphs = splitGraph(deletedNode);
        List<IQueryableBusSystem> newBusSystems = new ArrayList<>();
        for (Map<BlockPosition, List<BlockPosition>> newGraph: newGraphs) {
            BusSystemModel newBusSystem = new BusSystemModel(newGraph);
            newBusSystems.add(newBusSystem);
        }
        return newBusSystems;
    }

    /**
     * Removes one Node and his Edges and split Graph when needed.
     * @param deletedNode The Node to remove.
     * @return List of new Graphs.
     */
    private List<Map<BlockPosition, List<BlockPosition>>> splitGraph(BlockPosition deletedNode) {
        List<BlockPosition> neighbors = adjPositions.get(deletedNode);
        removeNode(deletedNode);
        //find new connected graphs
        List<Map<BlockPosition, List<BlockPosition>>> newGraphs = new ArrayList<>();
        outer: for(BlockPosition neighbor: neighbors){
            for(Map<BlockPosition, List<BlockPosition>> newGraph: newGraphs){
                if(newGraph.containsKey(neighbor))
                    continue outer;
            }
            newGraphs.add(bfs(neighbor));
        }
        return newGraphs;
    }

    /**
     * UNTESTED
     * BFS to find all connected nodes.
     * MOSTLY GENERATED WITH GITHUB COPILOT.
     * @param start The start node.
     * @return Map of all connected nodes.
     */
    private Map<BlockPosition, List<BlockPosition>> bfs(BlockPosition start) {
        Map<BlockPosition, List<BlockPosition>> discovered = new HashMap<>();
        Queue<BlockPosition> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            BlockPosition current = queue.poll();
            if (!discovered.containsKey(current)) {
                discovered.put(current, adjPositions.get(current));
                for (BlockPosition neighbor : adjPositions.get(current)) {
                    queue.add(neighbor);
                }
            }
        }
        return discovered;
    }

    /**
     * checks if a node is in the BusSystemModel
     * @param blockPosition is the node to check
     * @return true if the node is in the BusSystemModel
     */
    public boolean isNode(BlockPosition blockPosition) {
        return adjPositions.containsKey(blockPosition);
    }

}
