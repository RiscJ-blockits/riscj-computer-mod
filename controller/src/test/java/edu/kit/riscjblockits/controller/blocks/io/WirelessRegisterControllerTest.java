package edu.kit.riscjblockits.controller.blocks.io;

import edu.kit.riscjblockits.controller.blocks.IConnectableComputerBlockEntity;
import edu.kit.riscjblockits.model.blocks.BlockPosition;
import edu.kit.riscjblockits.model.blocks.WirelessRegisterModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static edu.kit.riscjblockits.model.blocks.RegisterModel.DEFAULT_WORD_LENGTH;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_XPOS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_YPOS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WIRELESS_ZPOS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class WirelessRegisterControllerTest {

    static WirelessRegisterController wirelessRegisterController;
    static WirelessRegisterModel wirelessRegisterModel;
    static Data data;

    @BeforeEach
    void setUp() {
        IConnectableComputerBlockEntity blockEntity = mock(IConnectableComputerBlockEntity.class);
        wirelessRegisterController = new WirelessRegisterController(blockEntity);
        wirelessRegisterModel = (WirelessRegisterModel) wirelessRegisterController.getModel();
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

    @Test
    void setData() {
        Data choseData = new Data();
        choseData.set("missing", new DataStringEntry("R1 R2 R3"));
        choseData.set("found", new DataStringEntry("R3 R4 R5"));
        data.set("registers", choseData);
        data.set(REGISTER_WORD_LENGTH, new DataStringEntry("32"));
        data.set("type", new DataStringEntry("IR"));
        wirelessRegisterController.setData(data);
        data = (Data) wirelessRegisterModel.getData();
        assertEquals("32", ((DataStringEntry) (data.get(REGISTER_WORD_LENGTH))).getContent());
        assertEquals("IR", ((DataStringEntry) (data.get("type"))).getContent());
        choseData = (Data) data.get("registers");
        assertEquals("R1 R2 R3", ((DataStringEntry) (choseData.get("missing"))).getContent());
        assertEquals("R3 R4 R5", ((DataStringEntry) (choseData.get("found"))).getContent());
    }

    @Test
    void setWrongLengthData() {
        data.set(REGISTER_WORD_LENGTH, new DataStringEntry(""));
        wirelessRegisterController.setData(data);
        assertEquals(String.valueOf(DEFAULT_WORD_LENGTH), ((DataStringEntry) ((Data) wirelessRegisterModel.getData()).get(REGISTER_WORD_LENGTH)).getContent());
    }

    @Test
    void setValueWithoutWord() {
        data.set(REGISTER_VALUE, new DataStringEntry("33"));
        wirelessRegisterController.setData(data);
        assertEquals("", ((DataStringEntry) ((Data) wirelessRegisterModel.getData()).get(REGISTER_VALUE)).getContent());
    }

    @Test
    void setWrongValue() {
        data.set(REGISTER_WORD_LENGTH, new DataStringEntry("4"));
        data.set(REGISTER_VALUE, new DataStringEntry("sdkjfh"));
        wirelessRegisterController.setData(data);
        assertEquals("", ((DataStringEntry) ((Data) wirelessRegisterModel.getData()).get(REGISTER_VALUE)).getContent());
    }

    @Test
    void setValue() {
        wirelessRegisterController.setNewValue(Value.fromHex("1A", 2));
        assertEquals("00001A", wirelessRegisterController.getValue().getHexadecimalValue());
    }

    @Test
    void getRegisterType() {
        assertEquals("[NOT_ASSIGNED]", wirelessRegisterController.getRegisterType());
        wirelessRegisterModel.setRegisterType("IR");
        assertEquals("IR", wirelessRegisterController.getRegisterType());
    }


}
