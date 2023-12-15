package edu.kit.riscjblockits.model;

import edu.kit.riscjblockits.model.blocks.BlockPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BusSystemModel {

    private Map<BlockPosition, List<BlockPosition>> adjPositions;

    BusSystemModel() {
        adjPositions = new HashMap<>();
    }

    public void addNode(BlockPosition newBlock) {
        adjPositions.put(newBlock, new ArrayList<>());
    }
// rethink publicity
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
        adjPositions.putAll(busSystemToCombine.getBusGraph());
        addEdge(newNode, ownNode);
    }

    public List<Map<BlockPosition, List<BlockPosition>>> split(BlockPosition deletedNode) {
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
}
