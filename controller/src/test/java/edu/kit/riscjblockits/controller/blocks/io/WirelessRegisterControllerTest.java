package edu.kit.riscjblockits.controller.blocks.io;

import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Equality;

import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_XPOS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_YPOS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_ZPOS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WirelessRegisterControllerTest {

    static WirelessRegisterController wirelessRegisterController;
    static Data data;

    @BeforeEach
    void setUp() {
        IConnectableComputerBlockEntity blockEntity = mock(IConnectableComputerBlockEntity.class);
        wirelessRegisterController = new WirelessRegisterController(blockEntity);
        data = new Data();
    }

    @Test
    void setWirelessData() {
        Data wData = new Data();
        wData.set(REGISTER_WIRELESS_XPOS, new DataStringEntry("1"));
        wData.set(REGISTER_WIRELESS_YPOS, new DataStringEntry("2"));
        wData.set(REGISTER_WIRELESS_ZPOS, new DataStringEntry("3"));
        data.set(REGISTER_WIRELESS, wData);
        wirelessRegisterController.setData(data);
        assertEquals(new BlockPosition(1,2,3), wirelessRegisterController.getConnectedPos());
    }

    @Test
    void setWrongWirelessData() {
        Data wData = new Data();
        wData.set(REGISTER_WIRELESS_XPOS, new DataStringEntry("fff"));
        wData.set(REGISTER_WIRELESS_YPOS, new DataStringEntry(""));
        wData.set(REGISTER_WIRELESS_ZPOS, new DataStringEntry("@"));
        data.set(REGISTER_WIRELESS, wData);
        wirelessRegisterController.setData(data);
        assertEquals(new BlockPosition(0,-300,0), wirelessRegisterController.getConnectedPos());
    }

    @Test
    void setWrongMissingWirelessData() {
        Data wData = new Data();
        data.set(REGISTER_WIRELESS, wData);
        wirelessRegisterController.setData(data);
        assertEquals(new BlockPosition(0,-300,0), wirelessRegisterController.getConnectedPos());
    }


    //ToDo copy test from register set Data Tests


}
