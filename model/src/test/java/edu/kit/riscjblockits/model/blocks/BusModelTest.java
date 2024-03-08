package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.busgraph.BusSystemModel;
import edu.kit.riscjblockits.model.busgraph.DataStub_BusSystemModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BusModelTest {

    private BusModel busModelUnderTest;

    @BeforeEach
    void setUp() {
        busModelUnderTest = new BusModel();
    }

    @Test
    void getData() {
        BusSystemModel bsm = new DataStub_BusSystemModel(Value.fromDecimal("45", 10), true);
        busModelUnderTest.setBelongingBusSystemModel(bsm);
        Data busData = (Data) busModelUnderTest.getData();
        assertEquals("true", ((IDataStringEntry) busData.get("active")).getContent());
        assertEquals("0000000000000000002D", ((IDataStringEntry) busData.get("presentData")).getContent());
    }

    @Test
    void getData2() {
        BusSystemModel bsm = new DataStub_BusSystemModel(Value.fromDecimal("45", 10), false);
        Data busData = (Data) busModelUnderTest.getData();
        assertEquals("false", ((IDataStringEntry) busData.get("active")).getContent());
    }

}
