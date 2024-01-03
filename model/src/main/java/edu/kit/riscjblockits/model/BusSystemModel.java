package edu.kit.riscjblockits.model;

import edu.kit.riscjblockits.model.blocks.BlockPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BusSystemModel implements IQuerybleBusSystem {

    private Map<BlockPosition, List<BlockPosition>> adjPositions;

    public BusSystemModel() {
        adjPositions = new HashMap<>();
    }

    public BusSystemModel(BlockPosition firstBlock) {
        adjPositions = new HashMap<>();
        addNode(firstBlock);
    }

    private BusSystemModel(Map<BlockPosition, List<BlockPosition>> adjPositions) {
        this.adjPositions = adjPositions;
    }
    public void addNode(BlockPosition newBlock) {
        adjPositions.put(newBlock, new ArrayList<>());
    }

    public void addEdge(BlockPosition pos1, BlockPosition pos2) {
        //ToDo kontrollieren on Knoten da, maybe check if actually adjacent
        adjPositions.get(pos1).add(pos2);
        adjPositions.get(pos2).add(pos1);
    }

    public void removeEdge(BlockPosition pos1, BlockPosition pos2) {
        adjPositions.get(pos1).remove(pos2);
        adjPositions.get(pos2).remove(pos1);
        //useless
    }

    public void removeNode(BlockPosition pos1) {
        for(BlockPosition pos2: adjPositions.get(pos1)){
            adjPositions.get(pos2).remove(pos1);
        }
        adjPositions.remove(pos1);
    }

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
    public void combineGraph(BlockPosition ownNode, BlockPosition newNode, BusSystemModel busSystemToCombine) {
        if (!busSystemToCombine.equals(this)) {
            adjPositions.putAll(busSystemToCombine.getBusGraph());
        }
        //adjPositions.putAll(busSystemToCombine.getBusGraph());
        //System.out.println("Graphsize:" + adjPositions.size());
        addEdge(newNode, ownNode);
    }

    public List<BusSystemModel> splitBusSystemModel(BlockPosition deletedNode) {
        List<Map<BlockPosition, List<BlockPosition>>> newGraphs = splitGraph(deletedNode);
        List<BusSystemModel> newBusSystems = new ArrayList<>();
        for (Map<BlockPosition, List<BlockPosition>> newGraph: newGraphs) {
            BusSystemModel newBusSystem = new BusSystemModel(newGraph);
            newBusSystems.add(newBusSystem);
        }
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
     * MOSTLY GENERATED WITH GITHUB COPILOT.
     * @param start
     * @return
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

    public boolean isNode(BlockPosition blockPosition) {
        return adjPositions.containsKey(blockPosition);
    }

}
