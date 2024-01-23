package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SystemClockModelTest {

    private SystemClockModel systemClockModel = new SystemClockModel();

    @BeforeEach
    void setUp() {
        systemClockModel = new SystemClockModel();
    }
    @Test
    void getData() {
        systemClockModel.setClockSpeed(44);
        systemClockModel.setActiveTick(true);
        systemClockModel.setClockMode(ClockMode.MC_TICK);
        Data clockData = (Data) systemClockModel.getData();
        assertEquals("44",((IDataStringEntry) clockData.get("speed")).getContent());
        assertEquals("MC_TICK",((IDataStringEntry) clockData.get("mode")).getContent());
        assertEquals("true",((IDataStringEntry) clockData.get("activeTick")).getContent());
    }

}