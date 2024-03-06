package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstructionSetModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_CLUSTERING;
import static edu.kit.riscjblockits.model.data.DataConstants.CONTROL_ITEM_PRESENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ControlUnitModelTest {

    ControlUnitModel controlUnitModel;

    @BeforeEach
    void init() {
        controlUnitModel = new ControlUnitModel();
    }

    @Test
    void getNoIstData() {
        controlUnitModel.setIstModel(null);
        Data controlUnitData = (Data) controlUnitModel.getData();
        assertEquals("false", ((IDataStringEntry) controlUnitData.get(CONTROL_ITEM_PRESENT)).getContent());
    }

    @Test
    void getIstData() {
        controlUnitModel.setIstModel(mock(IQueryableInstructionSetModel.class));
        Data controlUnitData = (Data) controlUnitModel.getData();
        assertEquals("true", ((IDataStringEntry) controlUnitData.get(CONTROL_ITEM_PRESENT)).getContent());
    }

    @Test
    void getClusteringData() {
        Data clusteringData = mock(Data.class);
        controlUnitModel.setClusteringData(clusteringData);
        Data controlUnitData = (Data) controlUnitModel.getData();
        assertEquals(clusteringData, controlUnitData.get(CONTROL_CLUSTERING));
    }

    @Test
    void getNoClusteringData() {
        controlUnitModel.setClusteringData(null);
        Data controlUnitData = (Data) controlUnitModel.getData();
        assertEquals(1, controlUnitData.getKeys().size());
    }

}
