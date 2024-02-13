package edu.kit.riscjblockits.controller.clustering;

import edu.kit.riscjblockits.controller.blocks.AluController;
import edu.kit.riscjblockits.controller.blocks.ArchiCheckStub_ClusterHandler;
import edu.kit.riscjblockits.controller.blocks.ArchiCheckStub_Entity;
import edu.kit.riscjblockits.controller.blocks.ArchiCheckStub_RegisterController;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.controller.blocks.IQueryableClusterController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.MemoryModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClusterArchitectureHandlerTest {

    @Disabled("Disabled because cast with stub does not work here")
    @Test
    void checkArchitecture() {
        ArchiCheckStub_RegisterController block1 = new ArchiCheckStub_RegisterController(new BlockPosition(0,0,0), "IAR", new java.util.ArrayList<>());
        ClusterHandler ch = block1.getClusterHandler();
        ch.checkFinished();
    }

    @Test
    void verySimpleCheckArchitectureWithRegister() {
        RegisterController block1 = new RegisterController(new ArchiCheckStub_Entity(new BlockPosition(0,0,0), new ArrayList<>()));
        Data rData = new Data();
        rData.set("type", new DataStringEntry("IAR"));
        block1.setData(rData);
        block1.startClustering(new BlockPosition(0,0,0));
        ClusterHandler ch = block1.getClusterHandler();
        //ch.checkFinished();
        assertFalse(ClusterArchitectureHandler.checkArchitecture(InstructionSetBuilder.buildInstructionSetModelMima(), ch));
    }

    @Test
    void simpleCheckArchitectureWithCUnit() {
        ControlUnitController block2 = new ControlUnitController(new ArchiCheckStub_Entity(new BlockPosition(1,0,0), new ArrayList<>()));
        block2.startClustering(new BlockPosition(0,0,0));
        ClusterHandler ch = block2.getClusterHandler();
        //ch.checkFinished();
        assertFalse(ClusterArchitectureHandler.checkArchitecture(InstructionSetBuilder.buildInstructionSetModelMima(), ch));
    }

    static List<IQueryableClusterController> blockController;
    static IQueryableInstructionSetModel istModel;


    static void setUp() {
        blockController = new ArrayList<>();
        istModel = InstructionSetBuilder.buildInstructionSetModelMima();
    }

    public RegisterController getR(String type) {
        RegisterController registerController = new RegisterController(new ArchiCheckStub_Entity());
        Data rData = new Data();
        rData.set("type", new DataStringEntry(type));
        registerController.setData(rData);
        return registerController;
    }

    @Order(1)
    @Test
    void checkArchitectureMIMA1_4() {
        setUp();

        blockController.add(getR("IAR"));
        blockController.add(getR("IR"));
        ArchiCheckStub_ClusterHandler clusterHandler = new ArchiCheckStub_ClusterHandler(blockController);
        assertFalse(ClusterArchitectureHandler.checkArchitecture(istModel, clusterHandler));
    }

    @Order(2)
    @Test
    void checkArchitectureMIMA2_4() {
        MemoryController memoryController = new MemoryController(new ArchiCheckStub_Entity());
        ((MemoryModel) memoryController.getModel()).setMemory(new Memory(10, 10));
        blockController.add(memoryController);
        blockController.add(new ControlUnitController(new ArchiCheckStub_Entity()));
        ArchiCheckStub_ClusterHandler clusterHandler = new ArchiCheckStub_ClusterHandler(blockController);
        assertFalse(ClusterArchitectureHandler.checkArchitecture(istModel, clusterHandler));
    }

    @Order(3)
    @Test
    void checkArchitectureMIMA3_4() {
        blockController.add(new SystemClockController(new ArchiCheckStub_Entity()));
        blockController.add(new AluController(new ArchiCheckStub_Entity()));
        blockController.add(getR("AKKU"));
        blockController.add(getR("X"));
        blockController.add(getR("Y"));
        blockController.add(getR("Z"));
        blockController.add(getR("SDR"));
        blockController.add(getR("SAR"));
        blockController.add(getR("EINS"));
        System.out.println(blockController.size());
        ArchiCheckStub_ClusterHandler clusterHandler = new ArchiCheckStub_ClusterHandler(blockController);
        assertTrue(ClusterArchitectureHandler.checkArchitecture(istModel, clusterHandler));
    }

    @Order(4)
    @Test
    void checkArchitectureMIMA4_4() {
        blockController.add(new MemoryController(new ArchiCheckStub_Entity()));
        ArchiCheckStub_ClusterHandler clusterHandler = new ArchiCheckStub_ClusterHandler(blockController);
        assertFalse(ClusterArchitectureHandler.checkArchitecture(istModel, clusterHandler));
    }

}
