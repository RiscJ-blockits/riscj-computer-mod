package edu.kit.riscjblockits.controller.simulation;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.controller.blocks.IQueryableSimController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.busgraph.BusSystemModel;
import edu.kit.riscjblockits.model.busgraph.IBusSystem;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.instructionset.InstructionBuildException;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SimulationSequenceHandlerTest {

    SimulationSequenceHandler handler;
    IRealtimeSimulationCallbackReceivable timeHandler;

    RegisterController fetch1Register;

    RegisterController test1Register;

    boolean simulationComplete;

    IBusSystem busSystem;

    private static InstructionSetModel buildInstructionSetModelTest() {
        InputStream is = InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetTestRun.jsonc");
        try {
            return InstructionSetBuilder.buildInstructionSetModel(is);
        }  catch (InstructionBuildException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        busSystem = new BusSystemModel(new BlockPosition(0, 0, 0));

        IRealtimeSimulationCallbackReceivable stub = () -> simulationComplete = true;
        this.timeHandler = stub;

        RegisterController registerController1 = mock(RegisterController.class);
        fetch1Register = registerController1;
        RegisterController registerController2 = mock(RegisterController.class);
        RegisterController registerController3 = mock(RegisterController.class);
        test1Register = registerController3;
        RegisterController registerController4 = mock(RegisterController.class);
        ControlUnitController controlUnitController = mock(ControlUnitController.class);
        MemoryController memory = mock(MemoryController.class);
        RegisterController programCounter = mock(RegisterController.class);
        when(programCounter.getRegisterType()).then((Answer<String>) invocation -> "fetch1");
        List<IQueryableSimController> controllers = new ArrayList<>();
        controllers.add(registerController1);
        controllers.add(registerController2);
        controllers.add(registerController3);
        controllers.add(registerController4);
        controllers.add(controlUnitController);
        controllers.add(memory);
        when(controlUnitController.getControllerType()).then((Answer< BlockControllerType >) invocation -> BlockControllerType.CONTROL_UNIT);
        when(registerController1.getControllerType()).then((Answer< BlockControllerType >) invocation -> BlockControllerType.REGISTER);
        when(registerController2.getControllerType()).then((Answer< BlockControllerType >) invocation -> BlockControllerType.REGISTER);
        when(registerController3.getControllerType()).then((Answer< BlockControllerType >) invocation -> BlockControllerType.REGISTER);
        when(registerController4.getControllerType()).then((Answer< BlockControllerType >) invocation -> BlockControllerType.REGISTER);
        when(memory.getControllerType()).then((Answer< BlockControllerType >) invocation -> BlockControllerType.MEMORY);
        when(registerController1.getRegisterType()).then((Answer<String>) invocation -> "fetch1");
        when(registerController2.getRegisterType()).then((Answer<String>) invocation -> "fetch2");
        when(registerController3.getRegisterType()).then((Answer<String>) i -> "test1" );
        when(registerController4.getRegisterType()).then((Answer<String>) i -> "test2" );
        when(registerController1.getValue()).then((Answer<Value>) invocation -> Value.fromBinary("0000", 4));
        when(registerController2.getValue()).then((Answer<Value>) invocation -> Value.fromBinary("0000", 4));
        when(controlUnitController.getInstructionSetModel())
                .then((Answer<IQueryableInstructionSetModel>) invocation -> buildInstructionSetModelTest());

        when(memory.getValue(any())).thenAnswer((Answer<Value>) invocation -> Value.fromBinary("0000", 4));
        when(registerController1.getBlockPosition()).then((Answer<BlockPosition>) i -> new BlockPosition(0, 0, 0) );
        when(registerController2.getBlockPosition()).then((Answer<BlockPosition>) i -> new BlockPosition(0, 0, 0) );
        when(registerController3.getBlockPosition()).then((Answer<BlockPosition>) i -> new BlockPosition(0, 0, 0) );
        when(registerController4.getBlockPosition()).then((Answer<BlockPosition>) i -> new BlockPosition(0, 0, 0) );

        this.handler = new SimulationSequenceHandler(controllers, busSystem, stub);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void runFetch() {
        handler.run();
        verify(fetch1Register, times(2)).setNewValue(any()); //change pc and move value to fetch 2
        verify(fetch1Register).activateVisualisation();
        assertTrue(simulationComplete);
    }

    @Test
    void runExecute() {
        handler.run();
        handler.run();
        verify(fetch1Register, times(2)).setNewValue(any()); //change pc and move value to fetch 2
        verify(fetch1Register).activateVisualisation();
        verify(test1Register, times(1)).setNewValue(any());
        verify(test1Register).activateVisualisation();
        assertTrue(simulationComplete);
    }

    @Test
    void resetVisualisation() {
        handler.resetVisualisation();
        assertFalse(((BusSystemModel)busSystem).getActiveVisualization(new BlockPosition(0, 0, 0)));
    }

    @Test
    void fullVisualisation() {
        handler.fullVisualisation();
        assertTrue(((BusSystemModel)busSystem).getActiveVisualization(new BlockPosition(0, 0, 0)));
    }

    @Test
    void setVisualizationMode() {
        handler.setVisualizationMode(SimulationSequenceHandler.VisualisationMode.FAST);
        handler.fullVisualisation();
        handler.resetVisualisation();
        assertTrue(((BusSystemModel)busSystem).getActiveVisualization(new BlockPosition(0, 0, 0)));
    }
}