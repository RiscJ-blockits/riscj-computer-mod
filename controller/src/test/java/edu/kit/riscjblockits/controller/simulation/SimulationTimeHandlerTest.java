package edu.kit.riscjblockits.controller.simulation;

import edu.kit.riscjblockits.controller.blocks.BlockControllerType;
import edu.kit.riscjblockits.controller.blocks.ControlUnitController;
import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.controller.blocks.IQueryableSimController;
import edu.kit.riscjblockits.controller.blocks.MemoryController;
import edu.kit.riscjblockits.controller.blocks.RegisterController;
import edu.kit.riscjblockits.controller.blocks.SystemClockController;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.ClockMode;
import edu.kit.riscjblockits.model.busgraph.IBusSystem;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import edu.kit.riscjblockits.model.instructionset.InstructionBuildException;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SimulationTimeHandlerTest {

    SimulationTimeHandler handler;
    SystemClockController clock;

    IBusSystem busSystem;

    RegisterController fetch1Register;
    RegisterController test1Register;
    private RegisterController registerController2;
    private RegisterController registerController4;
    private ControlUnitController controlUnitController;
    private MemoryController memory;
    private RegisterController programCounter;
    private IConnectableComputerBlockEntity blockEntity;

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

        handler = null;

        fetch1Register = mock(RegisterController.class);
        this.registerController2 = mock(RegisterController.class);
        test1Register = mock(RegisterController.class);
        this.registerController4 = mock(RegisterController.class);
        this.controlUnitController = mock(ControlUnitController.class);
        this.memory = mock(MemoryController.class);
        this.programCounter = mock(RegisterController.class);

        when(programCounter.getRegisterType()).then((Answer<String>) invocation -> "fetch1");

        blockEntity = mock(IConnectableComputerBlockEntity.class);
        clock = new SystemClockController(blockEntity);
        List<IQueryableSimController> controllers = new ArrayList<>();
        controllers.add(fetch1Register);
        controllers.add(registerController2);
        controllers.add(test1Register);
        controllers.add(registerController4);
        controllers.add(controlUnitController);
        controllers.add(memory);
        controllers.add(clock);
        busSystem = mock(IBusSystem.class);

        Mockito.reset(fetch1Register);
        Mockito.reset(test1Register);
        Mockito.reset(busSystem);
        Mockito.reset(registerController2);
        Mockito.reset(registerController4);
        Mockito.reset(controlUnitController);
        Mockito.reset(memory);
        Mockito.reset(programCounter);
        Mockito.reset(blockEntity);

        when(controlUnitController.getControllerType()).then((Answer<BlockControllerType>) invocation -> BlockControllerType.CONTROL_UNIT);
        when(fetch1Register.getControllerType()).then((Answer< BlockControllerType >) invocation -> BlockControllerType.REGISTER);
        when(registerController2.getControllerType()).then((Answer< BlockControllerType >) invocation -> BlockControllerType.REGISTER);
        when(test1Register.getControllerType()).then((Answer< BlockControllerType >) invocation -> BlockControllerType.REGISTER);
        when(registerController4.getControllerType()).then((Answer< BlockControllerType >) invocation -> BlockControllerType.REGISTER);
        when(memory.getControllerType()).then((Answer< BlockControllerType >) invocation -> BlockControllerType.MEMORY);
        when(fetch1Register.getRegisterType()).then((Answer<String>) invocation -> "fetch1");
        when(registerController2.getRegisterType()).then((Answer<String>) invocation -> "fetch2");
        when(test1Register.getRegisterType()).then((Answer<String>) i -> "test1" );
        when(registerController4.getRegisterType()).then((Answer<String>) i -> "test2" );
        when(fetch1Register.getValue()).then((Answer<Value>) invocation -> Value.fromBinary("0000", 4));
        when(registerController2.getValue()).then((Answer<Value>) invocation -> Value.fromBinary("0000", 4));
        when(controlUnitController.getInstructionSetModel())
                .then((Answer<IQueryableInstructionSetModel>) invocation -> buildInstructionSetModelTest());

        when(memory.getValue(any())).thenAnswer((Answer<Value>) invocation -> Value.fromBinary("0000", 4));
        when(fetch1Register.getBlockPosition()).then((Answer<BlockPosition>) i -> new BlockPosition(0, 0, 0) );
        when(registerController2.getBlockPosition()).then((Answer<BlockPosition>) i -> new BlockPosition(0, 0, 0) );
        when(test1Register.getBlockPosition()).then((Answer<BlockPosition>) i -> new BlockPosition(0, 0, 0) );
        when(registerController4.getBlockPosition()).then((Answer<BlockPosition>) i -> new BlockPosition(0, 0, 0) );

        handler = new SimulationTimeHandler(controllers, busSystem);
    }

    @AfterEach
    void tearDown() {


        fetch1Register = null;
        test1Register = null;
        busSystem = null;
        registerController2 = null;
        registerController4 = null;
        controlUnitController = null;
        memory = null;
        programCounter = null;
        blockEntity = null;

        handler = null;
    }

    @Test
    void onMinecraftTick() throws InterruptedException {

        Data data = new Data();
        data.set("mode", new DataStringEntry("MC_TICK"));
        data.set("speed", new DataStringEntry("2"));
        clock.setData(data);
        handler.updateObservedState();
        handler.onMinecraftTick();
        assertEquals(1, handler.getMinecraftTickCounter());
        sleep(1000);
        verify(fetch1Register, times(2)).setNewValue(any());
        verify(test1Register, never()).setNewValue(any());
    }

    @Test
    void onUserTickTrigger() throws InterruptedException {

        Data data = new Data();
        data.set("mode", new DataStringEntry("STEP"));
        clock.setData(data);
        handler.onUserTickTrigger();
        assertEquals(0, handler.getMinecraftTickCounter());
        sleep(1000);
        verify(fetch1Register, times(2)).setNewValue(any());
        verify(test1Register, never()).setNewValue(any());
    }

    @Test
    void onSimulationTickComplete() throws InterruptedException {

        Data data = new Data();
        data.set("mode", new DataStringEntry("REALTIME"));
        clock.setData(data);
        handler.onSimulationTickComplete();
        assertEquals(0, handler.getMinecraftTickCounter());
        sleep(1000); // avoid concurrent behaviour when testing the handler (never used in actual product)
        // will produce wrong numbers if verify of different tests are not separated by sleep time
        verify(test1Register, times(2)).setNewValue(any());
        verify(fetch1Register, times(2)).setNewValue(any());
    }

    @Test
    void updateObservedState() {

        Data data1 = new Data();
        data1.set("mode", new DataStringEntry("STEP"));
        clock.setData(data1);

        handler.updateObservedState();
        assertEquals(ClockMode.STEP, handler.getClockMode());

        Data data2 = new Data();
        data2.set("mode", new DataStringEntry("MC_TICK"));
        data2.set("speed", new DataStringEntry("2"));
        clock.setData(data2);

        handler.updateObservedState();
        assertEquals(ClockMode.MC_TICK, handler.getClockMode());
        assertEquals(2, handler.getClockSpeed());

        Data data3 = new Data();
        data3.set("mode", new DataStringEntry("REALTIME"));
        clock.setData(data3);

        handler.updateObservedState();
        assertEquals(ClockMode.REALTIME, handler.getClockMode());
    }
}