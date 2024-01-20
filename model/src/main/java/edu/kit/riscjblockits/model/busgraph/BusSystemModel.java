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
public class BusSystemModel implements IQueryableBusSystem {

    /**
     * Graph of all Blocks in the cluster and their connections.
     */
    private Map<BlockPosition, List<BlockPosition>> adjPositions;

    private Value presentData;
    private String from;
    private String to;

    /**
     * creates an empty BusSystemModel
     */
    public BusSystemModel() {
        adjPositions = new HashMap<>();
    }

    /**
     * creates a BusSystemModel with one node
     * @param firstBlock is the first node of the BusSystemModel
     */
    public BusSystemModel(BlockPosition firstBlock) {
        adjPositions = new HashMap<>();
        addNode(firstBlock);
    }

    /**
     * creates a BusSystemModel with the given graph
     * @param adjPositions is the graph of the new BusSystemModel
     */
    private BusSystemModel(Map<BlockPosition, List<BlockPosition>> adjPositions) {
        this.adjPositions = adjPositions;
        System.out.println("ModelSize: " + adjPositions.size());
    }

    public Value getPresentData(){
        return presentData;
    }

    public boolean getActiveVisualization(BlockPosition blockPosition) {
        //ToDo Berechne Pfade
        return false;
    }

    /**
     * adds a new node to the BusSystemModel
     * @param newBlock is the new node
     */
    public void addNode(BlockPosition newBlock) {
        adjPositions.put(newBlock, new ArrayList<>());
    }

    /**
     * adds an edge between two nodes
     * @param pos1 is the first node of the edge
     * @param pos2 is the second node of the edge
     */
    public void addEdge(BlockPosition pos1, BlockPosition pos2) {
        //ToDo kontrollieren ob Knoten da, maybe check if actually adjacent
        adjPositions.get(pos1).add(pos2);
        adjPositions.get(pos2).add(pos1);
    }

    /**
     * removes an edge between two nodes
     * @param pos1 is the first node of the edge
     * @param pos2 is the second node of the edge
     */
    public void removeEdge(BlockPosition pos1, BlockPosition pos2) {
        adjPositions.get(pos1).remove(pos2);
        adjPositions.get(pos2).remove(pos1);
        //useless
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
        //adjPositions.putAll(busSystemToCombine.getBusGraph());
        //System.out.println("Graphsize:" + adjPositions.size());
        addEdge(newNode, ownNode);
        System.out.println("ModelSize: " + adjPositions.size() );
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
        System.out.println("ModelSize: " + adjPositions.size());
        return newBusSystems;
    }

    /**
     * Only PUBLIC cause of JUnit-Test.
     * Removes one Node and his Edges and split Graph when needed.
     * @param deletedNode The Node to remove.
     * @return List of new Graphs.
     */
    public List<Map<BlockPosition, List<BlockPosition>>> splitGraph(BlockPosition deletedNode) {
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
