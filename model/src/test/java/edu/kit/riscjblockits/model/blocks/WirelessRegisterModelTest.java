package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_XPOS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_YPOS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_ZPOS;
import static org.junit.jupiter.api.Assertions.*;

class WirelessRegisterModelTest {

    WirelessRegisterModel wirelessRegisterModel;

    @BeforeEach
    void init() {
        wirelessRegisterModel = new WirelessRegisterModel();
    }

    @Test
    void getNeighbourData() {
       wirelessRegisterModel.setWirelessNeighbourPosition(new BlockPosition(1, 2, 3));
        Data wrData = (Data) wirelessRegisterModel.getData();
        Data data = (Data) wrData.get(REGISTER_WIRELESS);
        assertEquals("1.0", ((IDataStringEntry) data.get(REGISTER_WIRELESS_XPOS)).getContent());
        assertEquals("2.0", ((IDataStringEntry) data.get(REGISTER_WIRELESS_YPOS)).getContent());
        assertEquals("3.0", ((IDataStringEntry) data.get(REGISTER_WIRELESS_ZPOS)).getContent());
    }

}
