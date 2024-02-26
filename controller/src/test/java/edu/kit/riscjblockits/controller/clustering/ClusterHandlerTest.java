package edu.kit.riscjblockits.controller.clustering;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ClusteringStub_ComputerController;
import edu.kit.riscjblockits.controller.blocks.ClusteringStub_ControlUnit;
import edu.kit.riscjblockits.controller.blocks.IQueryableClusterController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
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
    void destroyingOrCombiningToANonBusBlock() {
        //create test Cluster
        ClusteringStub_ComputerController block1 = new ClusteringStub_ComputerController(new BlockPosition(1,0,0), BlockControllerType.MEMORY, blocks);
        allNeighbours.add(block1);
        ClusteringStub_ComputerController block2 = new ClusteringStub_ComputerController(new BlockPosition(0,1,0), BlockControllerType.BUS, blocks);
        ClusteringStub_ComputerController block3 = new ClusteringStub_ComputerController(new BlockPosition(0,0,1), BlockControllerType.BUS, blocks);
        busNeighbours.add(block2);
        busNeighbours.add(block3);
        allNeighbours.add(block2);
        allNeighbours.add(block3);
        ClusteringStub_ComputerController block4 = new ClusteringStub_ComputerController(new BlockPosition(0,1,1), BlockControllerType.BUS, busNeighbours);
        ClusteringStub_ComputerController block5 = new ClusteringStub_ComputerController(new BlockPosition(0,0,-1), BlockControllerType.BUS, blocks);
        allNeighbours.add(block5);
        ClusteringStub_ComputerController block6 = new ClusteringStub_ComputerController(new BlockPosition(0,0,0), BlockControllerType.MEMORY, allNeighbours);
        ClusterHandler testCluster = block6.getClusterHandler();

        assertEquals(1, testCluster.getBlocks().size());
        assertEquals(3, testCluster.getBusBlocks().size());
        assertEquals(2, testCluster.getBusSystemModel().getBusGraph().get(block6.getBlockPosition()).size());
        assertTrue(testCluster.getBusBlocks().contains(block2));
        assertTrue(testCluster.getBusBlocks().contains(block3));
        assertTrue(testCluster.getBusBlocks().contains(block4));

        allNeighbours.remove(block2);
        block6.setNeighbours(allNeighbours);
        block2.onBroken();

        testCluster = block6.getClusterHandler();

        assertEquals(1, testCluster.getBlocks().size());
        assertEquals(2, testCluster.getBusBlocks().size());
        assertEquals(1, testCluster.getBusSystemModel().getBusGraph().get(block6.getBlockPosition()).size());
        assertTrue(testCluster.getBusBlocks().contains(block3));
        assertTrue(testCluster.getBusBlocks().contains(block4));

        allNeighbours.remove(block3);
        block6.setNeighbours(allNeighbours);
        block3.onBroken();

        testCluster = block6.getClusterHandler();

        assertEquals(1, testCluster.getBlocks().size());
        assertEquals(1, testCluster.getBusBlocks().size());
        assertEquals(1, testCluster.getBusSystemModel().getBusGraph().get(block6.getBlockPosition()).size());
        assertTrue(testCluster.getBusBlocks().contains(block5));

        blocks.add(block4);
        blocks.add(block6);
        ClusteringStub_ComputerController block7 = new ClusteringStub_ComputerController(new BlockPosition(0,1,0), BlockControllerType.BUS, blocks);
        allNeighbours.add(block7);

        testCluster = block6.getClusterHandler();

        assertEquals(1, testCluster.getBlocks().size());
        assertEquals(1, testCluster.getBusBlocks().size());
        assertEquals(1, testCluster.getBusSystemModel().getBusGraph().get(block6.getBlockPosition()).size());
        assertTrue(testCluster.getBusBlocks().contains(block5));

        blocks = new ArrayList<>();
        blocks.add(block7);
        blocks.add(block5);
        ClusteringStub_ComputerController block8 = new ClusteringStub_ComputerController(new BlockPosition(0,1,-1), BlockControllerType.BUS, blocks);
        allNeighbours.add(block8);

        testCluster = block6.getClusterHandler();

        assertEquals(1, testCluster.getBlocks().size());
        assertEquals(4, testCluster.getBusBlocks().size());
        assertEquals(2, testCluster.getBusSystemModel().getBusGraph().get(block6.getBlockPosition()).size());
        assertTrue(testCluster.getBusBlocks().contains(block4));
        assertTrue(testCluster.getBusBlocks().contains(block5));
        assertTrue(testCluster.getBusBlocks().contains(block7));
        assertTrue(testCluster.getBusBlocks().contains(block8));

        allNeighbours.remove(block8);
        block6.setNeighbours(allNeighbours);
        block8.onBroken();

        testCluster = block6.getClusterHandler();

        assertEquals(1, testCluster.getBlocks().size());
        assertEquals(2, testCluster.getBusBlocks().size());
        assertEquals(1, testCluster.getBusSystemModel().getBusGraph().get(block6.getBlockPosition()).size());
        assertTrue(testCluster.getBusBlocks().contains(block4));
        assertTrue(testCluster.getBusBlocks().contains(block7));

        allNeighbours.remove(block7);
        block6.setNeighbours(allNeighbours);
        block7.onBroken();

        testCluster = block6.getClusterHandler();

        assertEquals(1, testCluster.getBlocks().size());
        assertEquals(1, testCluster.getBusBlocks().size());
        assertEquals(1, testCluster.getBusSystemModel().getBusGraph().get(block6.getBlockPosition()).size());
        assertTrue(testCluster.getBusBlocks().contains(block5));

        allNeighbours.remove(block5);
        block6.setNeighbours(allNeighbours);
        block5.onBroken();

        testCluster = block6.getClusterHandler();

        assertEquals(1, testCluster.getBlocks().size());
        assertEquals(0, testCluster.getBusBlocks().size());
        assertEquals(0, testCluster.getBusSystemModel().getBusGraph().get(block6.getBlockPosition()).size());
    }

    @Test
    void reClustringByDestroying() {
        ClusteringStub_ComputerController block1 = new ClusteringStub_ComputerController(new BlockPosition(1,0,0), BlockControllerType.MEMORY, blocks);
        blocks.add(block1);
        ClusteringStub_ComputerController block2 = new ClusteringStub_ComputerController(new BlockPosition(2,0,0), BlockControllerType.MEMORY, blocks);
        blocks = new ArrayList<>();
        blocks.add(block2);
        ClusteringStub_ComputerController block3 = new ClusteringStub_ComputerController(new BlockPosition(3,0,0), BlockControllerType.MEMORY, blocks);

        blocks = new ArrayList<>();
        ClusteringStub_ComputerController block4 = new ClusteringStub_ComputerController(new BlockPosition(0,1,0), BlockControllerType.BUS, blocks);
        blocks.add(block4);
        blocks.add(block1);
        ClusteringStub_ComputerController block5 = new ClusteringStub_ComputerController(new BlockPosition(1,1,0), BlockControllerType.BUS, blocks);
        block4.addNeighbour(block5);
        blocks = new ArrayList<>();
        blocks.add(block2);
        blocks.add(block5);
        ClusteringStub_ComputerController block6 = new ClusteringStub_ComputerController(new BlockPosition(2,1,0), BlockControllerType.BUS, blocks);
        block5.addNeighbour(block6);
        blocks = new ArrayList<>();
        blocks.add(block3);
        blocks.add(block6);
        ClusteringStub_ComputerController block7 = new ClusteringStub_ComputerController(new BlockPosition(3,1,0), BlockControllerType.BUS, blocks);
        block6.addNeighbour(block7);

        blocks = new ArrayList<>();
        ClusteringStub_ComputerController block8 = new ClusteringStub_ComputerController(new BlockPosition(0,-1,0), BlockControllerType.BUS, blocks);
        blocks = new ArrayList<>();
        blocks.add(block8);
        blocks.add(block1);
        ClusteringStub_ComputerController block9 = new ClusteringStub_ComputerController(new BlockPosition(1,-1,0), BlockControllerType.BUS, blocks);
        block8.addNeighbour(block9);
        blocks = new ArrayList<>();
        blocks.add(block2);
        blocks.add(block9);
        ClusteringStub_ComputerController block10 = new ClusteringStub_ComputerController(new BlockPosition(2,-1,0), BlockControllerType.BUS, blocks);
        block9.addNeighbour(block10);
        blocks = new ArrayList<>();
        blocks.add(block3);
        blocks.add(block10);
        ClusteringStub_ComputerController block11 = new ClusteringStub_ComputerController(new BlockPosition(3,-1,0), BlockControllerType.BUS, blocks);
        block10.addNeighbour(block11);

        blocks = new ArrayList<>();
        blocks.add(block5);
        blocks.add(block9);
        block1.setNeighbours(blocks);
        blocks = new ArrayList<>();
        blocks.add(block6);
        blocks.add(block10);
        block1.setNeighbours(blocks);
        blocks = new ArrayList<>();
        blocks.add(block7);
        blocks.add(block11);
        block1.setNeighbours(blocks);

        ClusterHandler testCluster = block7.getClusterHandler();
        assertEquals(3, testCluster.getBlocks().size());
        assertEquals(4, testCluster.getBusBlocks().size());
        testCluster = block8.getClusterHandler();
        assertEquals(0, testCluster.getBlocks().size());
        assertEquals(4, testCluster.getBusBlocks().size());

        blocks = new ArrayList<>();
        blocks.add(block4);
        blocks.add(block8);
        ClusteringStub_ComputerController block12 = new ClusteringStub_ComputerController(new BlockPosition(0,0,0), BlockControllerType.BUS, blocks);

        testCluster = block12.getClusterHandler();
        assertEquals(3, testCluster.getBlocks().size());
        assertEquals(9, testCluster.getBusBlocks().size());

        block12.onBroken();

        testCluster = block4.getClusterHandler();
        assertEquals(3, testCluster.getBlocks().size());
        assertEquals(4, testCluster.getBusBlocks().size());
        testCluster = block8.getClusterHandler();
        assertEquals(0, testCluster.getBlocks().size());
        assertEquals(4, testCluster.getBusBlocks().size());
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
        ClusteringStub_ComputerController block1 = new ClusteringStub_ComputerController(new BlockPosition(0,0,0) , BlockControllerType.BUS, blocks);
        blocks.add(block1);
        ClusteringStub_ComputerController block2 = new ClusteringStub_ComputerController(new BlockPosition(0,0,1), BlockControllerType.BUS, blocks);
        ClusteringStub_ComputerController block3 = new ClusteringStub_ComputerController(new BlockPosition(0,1,0), BlockControllerType.BUS, blocks);
        blocks = new ArrayList<>();
        blocks.add(block2);
        blocks.add(block3);
        ClusteringStub_ComputerController block4 = new ClusteringStub_ComputerController(new BlockPosition(0,1,1), BlockControllerType.BUS, blocks);

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

    @Test
    void setIstModel() {
        ClusteringStub_ControlUnit block1 = new ClusteringStub_ControlUnit(new BlockPosition(0,0,0), BlockControllerType.CONTROL_UNIT, blocks);
        allNeighbours.add(block1);
        ClusteringStub_ControlUnit block2 = new ClusteringStub_ControlUnit(new BlockPosition(2,0,0), BlockControllerType.CONTROL_UNIT, blocks);
        allNeighbours.add(block2);
        ClusteringStub_ComputerController block3 = new ClusteringStub_ComputerController(new BlockPosition(1,0,0), BlockControllerType.BUS, allNeighbours);

        ClusterHandler ch = block3.getClusterHandler();

        InstructionSetModel istModel = new InstructionSetModel();
        assertFalse(ch.setIstModel(istModel));
        assertFalse(ch.setIstModel(null));
    }
}