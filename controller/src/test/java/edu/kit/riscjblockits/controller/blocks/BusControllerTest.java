package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.controller.clustering.ClusterHandler;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.BusModel;
import edu.kit.riscjblockits.model.busgraph.BusSystemModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BusControllerTest {

    BusController testBusController;

    @BeforeEach
    void setUp() {
        testBusController = new BusController(mock(IConnectableComputerBlockEntity.class));
        ClusterHandler clusterHandler = mock(ClusterHandler.class);
        testBusController.setClusterHandler(clusterHandler);

        BusSystemModel busSystemModel = mock(BusSystemModel.class);
        when(clusterHandler.getBusSystemModel()).thenReturn(busSystemModel);
        when(busSystemModel.getBusSystemNeighbors(testBusController.getModel().getPosition())).thenReturn(List.of(new BlockPosition(1, 1, 1)));
    }

    @Test
    void createBlockModel() {
        assertNotNull(testBusController.createBlockModel());
    }

    @Test
    void getBusSystemNeighbors() {
        assertEquals(List.of(new BlockPosition(1, 1, 1)), testBusController.getBusSystemNeighbors());
    }

    @Test
    void setBusSystemModel() {
        BusSystemModel busSystemModel = mock(BusSystemModel.class);
        testBusController.setBusSystemModel(busSystemModel);
        BusModel busModel = (BusModel) testBusController.getModel();
        assertEquals(busSystemModel, busModel.getBelongsToSystem());
    }

    @Test
    void testNullGetBusSystemNeighbors() {
        testBusController = new BusController(mock(IConnectableComputerBlockEntity.class));
        assertNull(testBusController.getBusSystemNeighbors());
    }

}