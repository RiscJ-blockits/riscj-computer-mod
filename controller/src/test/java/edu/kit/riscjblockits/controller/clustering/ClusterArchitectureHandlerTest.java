package edu.kit.riscjblockits.controller.clustering;

import edu.kit.riscjblockits.controller.blocks.AluController;
import edu.kit.riscjblockits.controller.blocks.ArchiCheckStub_ClusterHandler;
import edu.kit.riscjblockits.controller.blocks.ArchiCheckStub_Entity;
import edu.kit.riscjblockits.controller.blocks.ArchiCheckStub_RegisterController;
import edu.kit.riscjblockits.controller.blocks.ComputerBlockController;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.controller.blocks.IQueryableClusterController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeAll
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

    @Test
    void checkArchitectureMIMA1_4() {
        blockController.add(getR("IAR"));
        blockController.add(getR("IR"));
        ArchiCheckStub_ClusterHandler clusterHandler = new ArchiCheckStub_ClusterHandler(blockController);
        assertFalse(ClusterArchitectureHandler.checkArchitecture(istModel, clusterHandler));
    }

    @Test
    void checkArchitectureMIMA2_4() {
        blockController.add(new MemoryController(new ArchiCheckStub_Entity()));
        blockController.add(new ControlUnitController(new ArchiCheckStub_Entity()));
        ArchiCheckStub_ClusterHandler clusterHandler = new ArchiCheckStub_ClusterHandler(blockController);
        assertFalse(ClusterArchitectureHandler.checkArchitecture(istModel, clusterHandler));
    }

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
        ArchiCheckStub_ClusterHandler clusterHandler = new ArchiCheckStub_ClusterHandler(blockController);
        assertTrue(ClusterArchitectureHandler.checkArchitecture(istModel, clusterHandler));
    }

    @Test
    void checkArchitectureMIMA4_4() {
        blockController.add(new MemoryController(new ArchiCheckStub_Entity()));
        ArchiCheckStub_ClusterHandler clusterHandler = new ArchiCheckStub_ClusterHandler(blockController);
        assertFalse(ClusterArchitectureHandler.checkArchitecture(istModel, clusterHandler));
    }

}
