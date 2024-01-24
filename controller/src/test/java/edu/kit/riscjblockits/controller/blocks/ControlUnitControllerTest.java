package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.ControlUnitModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControlUnitControllerTest {

    private ControlUnitController controlUnitController;

    @BeforeEach
    void init() {
        controlUnitController = new ControlUnitController(new ArchiCheckStub_Entity());
    }

    @Test
    void setData() {
        Data clusteringData = new Data();
        clusteringData.set("missingRegisters", new DataStringEntry("R1 R2 R3"));
        clusteringData.set("foundRegisters", new DataStringEntry("R4 R5 R6"));
        clusteringData.set("foundMemory", new DataStringEntry("1"));
        clusteringData.set("foundALU", new DataStringEntry("1"));
        clusteringData.set("foundControlUnit", new DataStringEntry("1"));
        clusteringData.set("foundSystemClock", new DataStringEntry("1"));
        Data cucData = new Data();
        cucData.set("clustering", clusteringData);
        controlUnitController.setData(cucData);
        ControlUnitModel controlUnitModel = (ControlUnitModel) controlUnitController.getModel();
        Data cmData = (Data) controlUnitModel.getData();
        clusteringData = (Data) cmData.get("clustering");
        assertEquals("R1 R2 R3", ((IDataStringEntry) (clusteringData.get("missingRegisters"))).getContent());
        assertEquals("R4 R5 R6", ((IDataStringEntry) (clusteringData.get("foundRegisters"))).getContent());
        assertEquals("1", ((IDataStringEntry) (clusteringData.get("foundMemory"))).getContent());
        assertEquals("1", ((IDataStringEntry) (clusteringData.get("foundALU"))).getContent());
        assertEquals("1", ((IDataStringEntry) (clusteringData.get("foundControlUnit"))).getContent());
        assertEquals("1", ((IDataStringEntry) (clusteringData.get("foundSystemClock"))).getContent());
    }

}