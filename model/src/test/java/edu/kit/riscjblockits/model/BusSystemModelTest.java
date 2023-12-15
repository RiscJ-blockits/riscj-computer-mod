package edu.kit.riscjblockits.model;

import edu.kit.riscjblockits.model.blocks.BlockPosition;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BusSystemModelTest {
    BusSystemModel system1 = new BusSystemModel();
    BusSystemModel system2 = new BusSystemModel();

    BusSystemModel system3 = new BusSystemModel();
    @Test
    void combineGraph() {
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
    void split() {
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

        List<Map<BlockPosition, List<BlockPosition>>> result = system1.split(new BlockPosition(0,1,1));

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
        List<Map<BlockPosition, List<BlockPosition>>> check = new ArrayList<>();
        check.add(system3.getBusGraph());
        check.add(system2.getBusGraph());
        assertEquals(result, check);
    }

    @Test
    void bfs() {
    }
}