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
        assertEquals(1, adjPositions.size());
        assertEquals(true, adjPositions.containsKey(new BlockPosition(0, 0, 0)));
        assertEquals(true, adjPositions.get(new BlockPosition(0, 0, 0)).isEmpty());
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

        assertEquals(true, system1.getActiveVisualization(graph.get(1)));
        assertEquals(true, system1.getActiveVisualization(graph.get(2)));
        assertEquals(false, system1.getActiveVisualization(graph.get(3)));
        assertEquals(true, system1.getActiveVisualization(graph.get(4)));
        assertEquals(true, system1.getActiveVisualization(graph.get(6)));
        assertEquals(true, system1.getActiveVisualization(graph.get(7)));
        assertEquals(true, system1.getActiveVisualization(graph.get(8)));
        assertEquals(false, system1.getActiveVisualization(graph.get(9)));
        assertEquals(false, system1.getActiveVisualization(graph.get(10)));
        assertEquals(false, system1.getActiveVisualization(graph.get(11)));
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

        List<BlockPosition> graph = new ArrayList<>();
        graph.add(new BlockPosition(0,0,0));
        graph.add(new BlockPosition(0,0,1));
        graph.add(new BlockPosition(0,1,0));
        graph.add(new BlockPosition(1,0,0));
        graph.add(new BlockPosition(1,0,1));
        graph.add(new BlockPosition(1,1,0));
        graph.add(new BlockPosition(0,1,1));
        for (BlockPosition pos : graph) {
            pos.setBus(true);
        }
        //Build result Graph
        system1.addNode(graph.get(0));
        system1.addNode(graph.get(1));
        system1.addNode(graph.get(2));
        system1.addEdge(graph.get(0), graph.get(1));
        system1.addEdge(graph.get(0), graph.get(2));
        system1.addNode(graph.get(3));
        system1.addNode(graph.get(4));
        system1.addNode(graph.get(5));
        system1.addEdge(graph.get(3), graph.get(4));
        system1.addEdge(graph.get(3), graph.get(5));
        system1.addEdge(graph.get(5), graph.get(4));
        system1.addNode(graph.get(6));
        system1.addEdge(graph.get(6), graph.get(1));
        system1.addEdge(graph.get(6), graph.get(2));
        system1.addEdge(graph.get(6), graph.get(3));

        List<IQueryableBusSystem> results = system1.splitBusSystemModel(graph.get(6));

        //Build third Graph
        system3.addNode(graph.get(0));
        system3.addNode(graph.get(1));
        system3.addNode(graph.get(2));
        system3.addEdge(graph.get(0), graph.get(1));
        system3.addEdge(graph.get(0), graph.get(2));
        //Build second Graph
        system2.addNode(graph.get(3));
        system2.addNode(graph.get(4));
        system2.addNode(graph.get(5));
        system2.addEdge(graph.get(3), graph.get(4));
        system2.addEdge(graph.get(3), graph.get(5));
        system2.addEdge(graph.get(5), graph.get(4));
        List<IQueryableBusSystem> check = new ArrayList<>();
        check.add(system3);
        check.add(system2);

        assertEquals(results.get(0).getBusGraph(), system3.getBusGraph());
        assertEquals(results.get(1).getBusGraph(), system2.getBusGraph());
    }

    @Test
    void splitBusSystemModelWithNonBusBlocks() {
        system1.removeNode(new BlockPosition(0,0,0));
        system2.removeNode(new BlockPosition(0,0,1));
        system3.removeNode(new BlockPosition(0,1,0));

        List<BlockPosition> graph = new ArrayList<>();
        graph.add(new BlockPosition(0,0,0));
        graph.add(new BlockPosition(0,0,1));
        graph.add(new BlockPosition(0,1,0));
        graph.add(new BlockPosition(1,0,0));
        graph.add(new BlockPosition(1,0,1));
        graph.add(new BlockPosition(1,1,0));
        graph.add(new BlockPosition(0,1,1));
        for (BlockPosition pos : graph) {
            pos.setBus(true);
        }
        graph.get(0).setBus(false);
        graph.get(4).setBus(false);
        //Build result Graph
        system1.addNode(graph.get(0));
        system1.addNode(graph.get(1));
        system1.addNode(graph.get(2));
        system1.addEdge(graph.get(0), graph.get(1));
        system1.addEdge(graph.get(0), graph.get(2));
        system1.addNode(graph.get(3));
        system1.addNode(graph.get(4));
        system1.addNode(graph.get(5));
        system1.addEdge(graph.get(3), graph.get(4));
        system1.addEdge(graph.get(3), graph.get(5));
        system1.addEdge(graph.get(5), graph.get(4));
        system1.addNode(graph.get(6));
        system1.addEdge(graph.get(6), graph.get(1));
        system1.addEdge(graph.get(6), graph.get(2));
        system1.addEdge(graph.get(6), graph.get(3));

        List<IQueryableBusSystem> results = system1.splitBusSystemModel(graph.get(6));
        //Building graph to compare
        system2.addNode(graph.get(3));
        system2.addNode(graph.get(4));
        system2.addNode(graph.get(5));
        system2.addEdge(graph.get(3), graph.get(5));
        system2.addEdge(graph.get(3), graph.get(4));
        system2.addEdge(graph.get(5), graph.get(4));

        assertEquals(results.get(2).getBusGraph(), system2.getBusGraph());

        system3.addNode(graph.get(0));
        system3.addNode(graph.get(1));
        system3.addEdge(graph.get(0), graph.get(1));

        assertEquals(results.get(0).getBusGraph(), system3.getBusGraph());

        system3 = new BusSystemModel(graph.get(2));

        assertEquals(results.get(1).getBusGraph(), system3.getBusGraph());
    }

    @Test
    void setAndResetVisualisation() {
        system1.activateVisualisation();
        assertTrue(system1.getActiveVisualization(new BlockPosition(0, 0, 0)));

        system1.resetVisualisation();
        assertFalse(system1.getActiveVisualization(new BlockPosition(0, 0, 0)));
    }

    @Test
    void presentData() {
        assertTrue(system1.getPresentData().equals(new Value()));
        system1.setBusDataPath(new BlockPosition(0, 0, 0), new BlockPosition(0, 0, 0), Value.fromBinary("00000000", 1));
        assertTrue(system1.getPresentData().equals(Value.fromBinary("00000000", 1)));
    }

    @Test
    void getBusSystemNeighbors() {
        system1.addNode(new BlockPosition(1, 0, 0));
        system1.addEdge(new BlockPosition(0, 0, 0), new BlockPosition(1, 0, 0));
        assertEquals(1, system1.getBusSystemNeighbors(new BlockPosition(0, 0, 0)).size());
    }
}