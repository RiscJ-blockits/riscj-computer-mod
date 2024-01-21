package edu.kit.riscjblockits.controller.clustering;

import edu.kit.riscjblockits.controller.blocks.ArchiCheckStub_Entity;
import edu.kit.riscjblockits.controller.blocks.ArchiCheckStub_RegisterController;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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
        RegisterController block1 = new RegisterController(new ArchiCheckStub_Entity(new BlockPosition(0,0,0)));
        block1.setData(new DataStringEntry("IAR"));
        block1.startClustering(new BlockPosition(0,0,0));
        ClusterHandler ch = block1.getClusterHandler();
        //ch.checkFinished();
        assertFalse(ClusterArchitectureHandler.checkArchitecture(InstructionSetBuilder.buildInstructionSetModelMima(), ch));
    }

    @Test
    void simpleCheckArchitectureWithCUnit() {
        ControlUnitController block2 = new ControlUnitController(new ArchiCheckStub_Entity(new BlockPosition(1,0,0)));
        block2.startClustering(new BlockPosition(0,0,0));
        ClusterHandler ch = block2.getClusterHandler();
        //ch.checkFinished();
        assertFalse(ClusterArchitectureHandler.checkArchitecture(InstructionSetBuilder.buildInstructionSetModelMima(), ch));
    }

}