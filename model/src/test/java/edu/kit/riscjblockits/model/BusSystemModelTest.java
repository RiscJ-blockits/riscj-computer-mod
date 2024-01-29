package edu.kit.riscjblockits.model;

import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.busgraph.BusSystemModel;
import edu.kit.riscjblockits.model.busgraph.IQueryableBusSystem;
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