package edu.kit.riscjblockits.model;

import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.busgraph.BusSystemModel;
import edu.kit.riscjblockits.model.busgraph.IQueryableBusSystem;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BusSystemModelTest {
    BusSystemModel system1 = new BusSystemModel(new BlockPosition(0,0,0));
    BusSystemModel system2 = new BusSystemModel(new BlockPosition(0,0,1));
    BusSystemModel system3 = new BusSystemModel(new BlockPosition(0,1,0));

    @Test
    void BusSystemModel() {
        Map<BlockPosition, List<BlockPosition>> adjPositions = system1.getBusGraph();
        assertEquals(adjPositions.size(), 1);
        assertEquals(adjPositions.containsKey(new BlockPosition(0,0,0)), true);
        assertEquals(adjPositions.get(new BlockPosition(0,0,0)).isEmpty(), true);
    }

    @Test
    void setBusDataPath() {
        system1.removeNode(new BlockPosition(0,0,0));
        system2.removeNode(new BlockPosition(0,0,1));
        system3.removeNode(new BlockPosition(0,1,0));
        //Build test Graph
        List<BlockPosition> graph = new ArrayList<>();
        graph.add(new BlockPosition(0,0,0));
        graph.add(new BlockPosition(0,0,1));
        graph.add(new BlockPosition(0,0,2));
        graph.add(new BlockPosition(0,0,3));
        graph.add(new BlockPosition(0,0,4));
        graph.add(new BlockPosition(0,0,5));

        graph.add(new BlockPosition(0,1,2));
        graph.add(new BlockPosition(0,1,3));
        graph.add(new BlockPosition(0,1,4));

        graph.add(new BlockPosition(0,2,2));
        graph.add(new BlockPosition(0,2,3));
        graph.add(new BlockPosition(0,2,4));

        graph.get(0).setBus(false);
        graph.get(1).setBus(true);
        graph.get(2).setBus(true);
        graph.get(3).setBus(false);
        graph.get(4).setBus(true);
        graph.get(5).setBus(false);

        graph.get(6).setBus(true);
        graph.get(7).setBus(true);
        graph.get(8).setBus(true);

        graph.get(9).setBus(true);
        graph.get(10).setBus(true);
        graph.get(11).setBus(true);

        for (BlockPosition pos : graph) {
            system1.addNode(pos);
        }

        system1.addEdge(graph.get(0), graph.get(1));
        system1.addEdge(graph.get(1), graph.get(2));
        system1.addEdge(graph.get(2), graph.get(3));
        system1.addEdge(graph.get(3), graph.get(4));
        system1.addEdge(graph.get(4), graph.get(5));

        system1.addEdge(graph.get(6), graph.get(7));
        system1.addEdge(graph.get(7), graph.get(8));

        system1.addEdge(graph.get(9), graph.get(10));
        system1.addEdge(graph.get(10), graph.get(11));

        system1.addEdge(graph.get(2), graph.get(6));
        system1.addEdge(graph.get(6), graph.get(9));
        system1.addEdge(graph.get(4), graph.get(8));
        system1.addEdge(graph.get(8), graph.get(11));

        system1.setBusDataPath(graph.get(0), graph.get(5), new Value());

        assertEquals(system1.getActiveVisualization(graph.get(1)), true);
        assertEquals(system1.getActiveVisualization(graph.get(2)), true);
        assertEquals(system1.getActiveVisualization(graph.get(3)), false);
        assertEquals(system1.getActiveVisualization(graph.get(4)), true);
        assertEquals(system1.getActiveVisualization(graph.get(6)), true);
        assertEquals(system1.getActiveVisualization(graph.get(7)), true);
        assertEquals(system1.getActiveVisualization(graph.get(8)), true);
        assertEquals(system1.getActiveVisualization(graph.get(9)), false);
        assertEquals(system1.getActiveVisualization(graph.get(10)), false);
        assertEquals(system1.getActiveVisualization(graph.get(11)), false);
    }
    @Test
    void combineGraph() {
        system1.removeNode(new BlockPosition(0,0,0));
        system2.removeNode(new BlockPosition(0,0,1));
        system3.removeNode(new BlockPosition(0,1,0));
        //Build result Graph
        system3.addNode(new BlockPosition(0,0,0));
        system3.addNode(new BlockPosition(0,0,1));
        system3.addNode(new BlockPosition(0,1,0));
        system3.addEdge(new BlockPosition(0,0,0), new BlockPosition(0,0,1));
        system3.addEdge(new BlockPosition(0,0,0), new BlockPosition(0,1,0));
        system3.addNode(new BlockPosition(1,0,0));
        system3.addNode(new BlockPosition(1,0,1));
        system3.addNode(new BlockPosition(1,1,0));
        system3.addEdge(new BlockPosition(1,0,0), new BlockPosition(1,0,1));
        system3.addEdge(new BlockPosition(1,0,0), new BlockPosition(1,1,0));
        system3.addEdge(new BlockPosition(1,1,0), new BlockPosition(1,0,1));
        system3.addNode(new BlockPosition(0,1,1));
        system3.addEdge(new BlockPosition(0,0,1), new BlockPosition(0,1,1));
        system3.addEdge(new BlockPosition(0,1,0), new BlockPosition(0,1,1));
        system3.addEdge(new BlockPosition(0,1,1), new BlockPosition(1,0,0));

        //Build first Graph
        system1.addNode(new BlockPosition(0,0,0));
        system1.addNode(new BlockPosition(0,0,1));
        system1.addNode(new BlockPosition(0,1,0));
        system1.addEdge(new BlockPosition(0,0,0), new BlockPosition(0,0,1));
        system1.addEdge(new BlockPosition(0,0,0), new BlockPosition(0,1,0));
        //Build second Graph
        system2.addNode(new BlockPosition(1,0,0));
        system2.addNode(new BlockPosition(1,0,1));
        system2.addNode(new BlockPosition(1,1,0));
        system2.addEdge(new BlockPosition(1,0,0), new BlockPosition(1,0,1));
        system2.addEdge(new BlockPosition(1,0,0), new BlockPosition(1,1,0));
        system2.addEdge(new BlockPosition(1,1,0), new BlockPosition(1,0,1));
        //Combine the Systems
        system1.addNode(new BlockPosition(0,1,1));
        system1.addEdge(new BlockPosition(0,1,1), new BlockPosition(0,0,1));
        system1.addEdge(new BlockPosition(0,1,1), new BlockPosition(0,1,0));
        system1.combineGraph(new BlockPosition(0,1,1), new BlockPosition(1,0,0), system2);
        assertEquals(system1.getBusGraph(), system3.getBusGraph());
    }

    @Test
    void combineGraphWithItself() {
        //Build test Graph
        system1.addNode(new BlockPosition(0,0,0));
        system1.addNode(new BlockPosition(0,0,1));
        system1.addNode(new BlockPosition(0,1,0));
        system1.addEdge(new BlockPosition(0,0,0), new BlockPosition(0,0,1));
        system1.addEdge(new BlockPosition(0,0,0), new BlockPosition(0,1,0));
        system1.addNode(new BlockPosition(1,0,0));
        system1.addNode(new BlockPosition(1,0,1));
        system1.addNode(new BlockPosition(1,1,0));
        system1.addEdge(new BlockPosition(1,0,0), new BlockPosition(1,0,1));
        system1.addEdge(new BlockPosition(1,0,0), new BlockPosition(1,1,0));
        system1.addEdge(new BlockPosition(1,1,0), new BlockPosition(1,0,1));
        system1.addNode(new BlockPosition(0,1,1));
        system1.addEdge(new BlockPosition(0,0,1), new BlockPosition(0,1,1));
        system1.addEdge(new BlockPosition(0,1,0), new BlockPosition(0,1,1));
        system1.addEdge(new BlockPosition(0,1,1), new BlockPosition(1,0,0));
        //Build result Graph
        system2.addNode(new BlockPosition(0,0,0));
        system2.addNode(new BlockPosition(0,0,1));
        system2.addNode(new BlockPosition(0,1,0));
        system2.addEdge(new BlockPosition(0,0,0), new BlockPosition(0,0,1));
        system2.addEdge(new BlockPosition(0,0,0), new BlockPosition(0,1,0));
        system2.addNode(new BlockPosition(1,0,0));
        system2.addNode(new BlockPosition(1,0,1));
        system2.addNode(new BlockPosition(1,1,0));
        system2.addEdge(new BlockPosition(1,0,0), new BlockPosition(1,0,1));
        system2.addEdge(new BlockPosition(1,0,0), new BlockPosition(1,1,0));
        system2.addEdge(new BlockPosition(1,1,0), new BlockPosition(1,0,1));
        system2.addNode(new BlockPosition(0,1,1));
        system2.addEdge(new BlockPosition(0,0,1), new BlockPosition(0,1,1));
        system2.addEdge(new BlockPosition(0,1,0), new BlockPosition(0,1,1));
        system2.addEdge(new BlockPosition(0,1,1), new BlockPosition(1,0,0));

        system1.combineGraph(new BlockPosition(0,0,1), new BlockPosition(0,1,0), system1);

        assertEquals(system1.getBusGraph().size(), system2.getBusGraph().size());
        int edgeCountSystem1 = 0;
        int edgeCountSystem2 = 0;
        for (BlockPosition pos : system1.getBusGraph().keySet()) {
            edgeCountSystem1 += system1.getBusGraph().get(pos).size();
        }
        for (BlockPosition pos : system2.getBusGraph().keySet()) {
            edgeCountSystem2 += system2.getBusGraph().get(pos).size();
        }
        assertEquals(edgeCountSystem1 - 2, edgeCountSystem2);
    }

    @Test
    void splitBusSystemModel() {
        system1.removeNode(new BlockPosition(0,0,0));
        system2.removeNode(new BlockPosition(0,0,1));
        system3.removeNode(new BlockPosition(0,1,0));
        //Build result Graph
        system1.addNode(new BlockPosition(0,0,0));
        system1.addNode(new BlockPosition(0,0,1));
        system1.addNode(new BlockPosition(0,1,0));
        system1.addEdge(new BlockPosition(0,0,0), new BlockPosition(0,0,1));
        system1.addEdge(new BlockPosition(0,0,0), new BlockPosition(0,1,0));
        system1.addNode(new BlockPosition(1,0,0));
        system1.addNode(new BlockPosition(1,0,1));
        system1.addNode(new BlockPosition(1,1,0));
        system1.addEdge(new BlockPosition(1,0,0), new BlockPosition(1,0,1));
        system1.addEdge(new BlockPosition(1,0,0), new BlockPosition(1,1,0));
        system1.addEdge(new BlockPosition(1,1,0), new BlockPosition(1,0,1));
        system1.addNode(new BlockPosition(0,1,1));
        system1.addEdge(new BlockPosition(0,1,1), new BlockPosition(0,0,1));
        system1.addEdge(new BlockPosition(0,1,1), new BlockPosition(0,1,0));
        system1.addEdge(new BlockPosition(0,1,1), new BlockPosition(1,0,0));

        List<IQueryableBusSystem> results = system1.splitBusSystemModel(new BlockPosition(0,1,1));

        //Build third Graph
        system3.addNode(new BlockPosition(0,0,0));
        system3.addNode(new BlockPosition(0,0,1));
        system3.addNode(new BlockPosition(0,1,0));
        system3.addEdge(new BlockPosition(0,0,0), new BlockPosition(0,0,1));
        system3.addEdge(new BlockPosition(0,0,0), new BlockPosition(0,1,0));
        //Build second Graph
        system2.addNode(new BlockPosition(1,0,0));
        system2.addNode(new BlockPosition(1,0,1));
        system2.addNode(new BlockPosition(1,1,0));
        system2.addEdge(new BlockPosition(1,0,0), new BlockPosition(1,0,1));
        system2.addEdge(new BlockPosition(1,0,0), new BlockPosition(1,1,0));
        system2.addEdge(new BlockPosition(1,1,0), new BlockPosition(1,0,1));
        List<IQueryableBusSystem> check = new ArrayList<>();
        check.add(system3);
        check.add(system2);

        assertEquals(results.get(0).getBusGraph(), system3.getBusGraph());
        assertEquals(results.get(1).getBusGraph(), system2.getBusGraph());
    }
}