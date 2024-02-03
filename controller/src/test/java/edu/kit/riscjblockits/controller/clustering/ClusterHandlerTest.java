package edu.kit.riscjblockits.controller.clustering;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ClusteringStub_ComputerController;
import edu.kit.riscjblockits.controller.blocks.IQueryableClusterController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClusterHandlerTest {

    List<IQueryableClusterController> blocks;
    List<IQueryableClusterController> allNeighbours;
    List<IQueryableClusterController> busNeighbours;

    @BeforeEach
    public void initObjects() {
        blocks = new java.util.ArrayList<>();
        allNeighbours = new java.util.ArrayList<>();
        busNeighbours = new java.util.ArrayList<>();
    }

    @Test
    void simpleCombine() {
        ClusteringStub_ComputerController block1 = new ClusteringStub_ComputerController(new BlockPosition(0,0,0), BlockControllerType.MEMORY, blocks);
        allNeighbours.add(block1);
        ClusteringStub_ComputerController block2 = new ClusteringStub_ComputerController(new BlockPosition(2,0,0), BlockControllerType.MEMORY, blocks);
        allNeighbours.add(block2);
        ClusteringStub_ComputerController block3 = new ClusteringStub_ComputerController(new BlockPosition(1,0,0), BlockControllerType.BUS, allNeighbours);
        ClusterHandler ch = block1.getClusterHandler();
        assertEquals(2, ch.getBlocks().size());
        assertEquals(1, ch.getBusBlocks().size());
    }

    @Test
    void simpleCombineBus() {
        ClusteringStub_ComputerController block1 = new ClusteringStub_ComputerController(new BlockPosition(0,0,0), BlockControllerType.BUS, allNeighbours);
        allNeighbours.add(block1);
        ClusteringStub_ComputerController block2 = new ClusteringStub_ComputerController(new BlockPosition(1,0,0), BlockControllerType.BUS, allNeighbours);
        allNeighbours.add(block2);
        ClusterHandler ch = block1.getClusterHandler();
        assertEquals(0, ch.getBlocks().size());
        assertEquals(2, ch.getBusBlocks().size());
    }

    @Test
    void simpleDestroy() {
        ClusteringStub_ComputerController block1 = new ClusteringStub_ComputerController(new BlockPosition(0,0,0), BlockControllerType.MEMORY, blocks);
        ClusteringStub_ComputerController block2 = new ClusteringStub_ComputerController(new BlockPosition(3,0,0), BlockControllerType.MEMORY, blocks);
        blocks.add(block1);
        ClusteringStub_ComputerController block3 = new ClusteringStub_ComputerController(new BlockPosition(1,0,0), BlockControllerType.BUS, blocks);
        blocks = new ArrayList<>();
        blocks.add(block2);
        blocks.add(block3);
        ClusteringStub_ComputerController block4 = new ClusteringStub_ComputerController(new BlockPosition(2,0,0), BlockControllerType.BUS, blocks);

        ClusterHandler ch = block1.getClusterHandler();
        assertEquals(2, ch.getBlocks().size());
        assertEquals(2, ch.getBusBlocks().size());

        block3.onBroken();
        ClusterHandler ch2 = block1.getClusterHandler();
        assertEquals(1, ch2.getBlocks().size());
        assertEquals(0, ch2.getBusBlocks().size());

        ClusterHandler ch3 = block2.getClusterHandler();
        assertEquals(1, ch3.getBlocks().size());
        assertEquals(1, ch3.getBusBlocks().size());
    }

    @Test
    void destroyWithoutSplit() {
        List<BlockPosition> graph = new ArrayList<>();
        graph.add(new BlockPosition(0,0,0));
        graph.add(new BlockPosition(0,0,1));
        graph.add(new BlockPosition(0,1,0));
        graph.add(new BlockPosition(0,1,1));
        for (BlockPosition pos : graph) {
            pos.setBus(true);
        }
        ClusteringStub_ComputerController block1 = new ClusteringStub_ComputerController(graph.get(0), BlockControllerType.BUS, blocks);
        blocks.add(block1);
        ClusteringStub_ComputerController block2 = new ClusteringStub_ComputerController(graph.get(1), BlockControllerType.BUS, blocks);
        ClusteringStub_ComputerController block3 = new ClusteringStub_ComputerController(graph.get(2), BlockControllerType.BUS, blocks);
        blocks = new ArrayList<>();
        blocks.add(block2);
        blocks.add(block3);
        ClusteringStub_ComputerController block4 = new ClusteringStub_ComputerController(graph.get(3), BlockControllerType.BUS, blocks);

        ClusterHandler cluster = block2.getClusterHandler();
        assertEquals(4, cluster.getBusBlocks().size());

        block1.onBroken();
        cluster = block2.getClusterHandler();
        assertEquals(3, cluster.getBusBlocks().size());
    }

    @Test
    void oneBlockReCombine() {
        ClusteringStub_ComputerController block1 = new ClusteringStub_ComputerController(new BlockPosition(0,0,0), BlockControllerType.BUS, blocks);
        allNeighbours.add(block1);
        ClusteringStub_ComputerController block2 = new ClusteringStub_ComputerController(new BlockPosition(2,0,0), BlockControllerType.BUS, blocks);
        allNeighbours.add(block2);
        ClusteringStub_ComputerController block3 = new ClusteringStub_ComputerController(new BlockPosition(1,0,0), BlockControllerType.MEMORY, allNeighbours);
        ClusterHandler firstCluster = block2.getClusterHandler();
        ClusterHandler secondCluster = block3.getClusterHandler();
        assertEquals(0, firstCluster.getBlocks().size());
        assertEquals(1, secondCluster.getBlocks().size());
        assertEquals(1, firstCluster.getBusBlocks().size());
        assertEquals(1, secondCluster.getBusBlocks().size());

        block1.onBroken();

        firstCluster = block2.getClusterHandler();
        secondCluster = block3.getClusterHandler();
        assertEquals(secondCluster.getBlocks().size(), firstCluster.getBlocks().size());
        assertEquals(secondCluster.getBusBlocks().size(), firstCluster.getBusBlocks().size());
    }
}