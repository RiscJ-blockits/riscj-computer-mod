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
        assertEquals(1, ch.getBlocks().size());
        assertEquals(0, ch.getBusBlocks().size());

        ClusterHandler ch3 = block1.getClusterHandler();
        assertEquals(1, ch.getBlocks().size());
        assertEquals(1, ch.getBusBlocks().size());
    }

}