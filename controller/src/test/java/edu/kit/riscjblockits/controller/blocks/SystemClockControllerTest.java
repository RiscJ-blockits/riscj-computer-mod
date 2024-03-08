package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.simulation.SimulationTimeHandler;
import edu.kit.riscjblockits.model.blocks.ClockMode;
import edu.kit.riscjblockits.model.blocks.SystemClockModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_MODE;
import static edu.kit.riscjblockits.model.data.DataConstants.CLOCK_SPEED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SystemClockControllerTest {

    private SystemClockController systemClockController;
    Data clockData;

    @BeforeEach
    void setUp() {
        systemClockController = new SystemClockController(new ArchiCheckStub_Entity());
        clockData = new Data();
    }

    @Test
    void setData() {
        clockData.set(CLOCK_SPEED, new DataStringEntry("42"));
        clockData.set(CLOCK_MODE, new DataStringEntry("MC_TICK"));
        systemClockController.setData(clockData);
        SystemClockModel clockModel = ((SystemClockModel) systemClockController.getModel());
        assertEquals(42, clockModel.getClockSpeed());
        assertEquals(ClockMode.MC_TICK, clockModel.getClockMode());
    }

    @Test
    void setWrongData() {
        clockData.set(CLOCK_SPEED, new DataStringEntry("ysdxfdgs"));
        clockData.set(CLOCK_MODE, new DataStringEntry("<fdssf"));
        systemClockController.setData(clockData);
        SystemClockModel clockModel = ((SystemClockModel) systemClockController.getModel());
        assertEquals(1, clockModel.getClockSpeed());
        assertEquals(ClockMode.STEP, clockModel.getClockMode());
    }

    @Test
    void testSetControllerType() {
        systemClockController.setControllerType(BlockControllerType.CLOCK);
        assertEquals(BlockControllerType.CLOCK, systemClockController.getControllerType());
    }

    @Test
    void testTickTrigger() {
        SimulationTimeHandler mockTimeHandler = mock(SimulationTimeHandler.class);
        systemClockController.createBlockModel();
        systemClockController.setSimulationTimeHandler(mockTimeHandler);
        doNothing().when(mockTimeHandler).onUserTickTrigger();
        doNothing().when(mockTimeHandler).onMinecraftTick();

        systemClockController.tick();
        verify(mockTimeHandler).onMinecraftTick();

        systemClockController.onUserTickTriggered();
        verify(mockTimeHandler).onUserTickTrigger();

    }

}
