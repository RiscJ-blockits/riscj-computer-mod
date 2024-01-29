package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.ClockMode;
import edu.kit.riscjblockits.model.blocks.SystemClockModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemClockControllerTest {

    private SystemClockController systemClockControllerUnderTest;

    @BeforeEach
    void setUp() {
        systemClockControllerUnderTest = new SystemClockController(new ArchiCheckStub_Entity());
    }

    @Test
    void setData() {
        Data clockData = new Data();
        clockData.set("speed", new DataStringEntry("42"));
        clockData.set("mode", new DataStringEntry("MC_TICK"));
        systemClockControllerUnderTest.setData(clockData);
        SystemClockModel clockModel = ((SystemClockModel) systemClockControllerUnderTest.getModel());
        assertEquals(42, clockModel.getClockSpeed());
        assertEquals(ClockMode.MC_TICK, clockModel.getClockMode());
    }

}