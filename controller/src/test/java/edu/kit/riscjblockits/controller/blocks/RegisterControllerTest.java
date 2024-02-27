package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static edu.kit.riscjblockits.model.blocks.RegisterModel.DEFAULT_WORD_LENGTH;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_ALU_REGS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_FOUND;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_MISSING;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_REGISTERS;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_VALUE;
import static edu.kit.riscjblockits.model.data.DataConstants.REGISTER_WORD_LENGTH;
import static org.junit.jupiter.api.Assertions.*;

class RegisterControllerTest {

    RegisterController registerController;
    Data rData;
    RegisterModel registerModel;

    @BeforeEach
    void init() {
        registerController = new RegisterController(new ArchiCheckStub_Entity());
        registerModel = (RegisterModel) registerController.getModel();
        rData = new Data();
    }

    @Test
    void setData() {
        Data choseData = new Data();
        choseData.set("missing", new DataStringEntry("R1 R2 R3"));
        choseData.set("found", new DataStringEntry("R3 R4 R5"));
        rData.set("registers", choseData);
        rData.set(REGISTER_WORD_LENGTH, new DataStringEntry("32"));
        rData.set("type", new DataStringEntry("IR"));
        registerController.setData(rData);
        rData = (Data) registerModel.getData();
        assertEquals("32", ((DataStringEntry) (rData.get(REGISTER_WORD_LENGTH))).getContent());
        assertEquals("IR", ((DataStringEntry) (rData.get("type"))).getContent());
        choseData = (Data) rData.get("registers");
        assertEquals("R1 R2 R3", ((DataStringEntry) (choseData.get("missing"))).getContent());
        assertEquals("R3 R4 R5", ((DataStringEntry) (choseData.get("found"))).getContent());
    }

    @Test
    void setWrongLengthData() {
        rData.set(REGISTER_WORD_LENGTH, new DataStringEntry(""));
        registerController.setData(rData);
        assertEquals(String.valueOf(DEFAULT_WORD_LENGTH), ((DataStringEntry) ((Data) registerModel.getData()).get(REGISTER_WORD_LENGTH)).getContent());
    }

    @Test
    void setValueWithoutWord() {
        rData.set(REGISTER_VALUE, new DataStringEntry("33"));
        registerController.setData(rData);
        assertEquals("", ((DataStringEntry) ((Data) registerModel.getData()).get(REGISTER_VALUE)).getContent());
    }

    @Test
    void setWrongValue() {
        rData.set(REGISTER_WORD_LENGTH, new DataStringEntry("4"));
        rData.set(REGISTER_VALUE, new DataStringEntry("sdkjfh"));
        registerController.setData(rData);
        assertEquals("", ((DataStringEntry) ((Data) registerModel.getData()).get(REGISTER_VALUE)).getContent());
    }

    @Test
    void setClusteringDataWrong() {
        Data choseData = new Data();
        choseData.set(REGISTER_MISSING, new DataStringEntry("R1 R2 R3"));
        //missing second data
        rData.set(REGISTER_REGISTERS, choseData);
        registerController.setData(rData);
        rData = (Data) registerModel.getData();
        choseData = (Data) rData.get(REGISTER_REGISTERS);
        assertEquals(0, choseData.getKeys().size());
    }

    @Test
    void setClusteringData() {
        Data choseData = new Data();
        choseData.set(REGISTER_MISSING, new DataStringEntry("R1 R2 R3"));
        choseData.set(REGISTER_FOUND, new DataStringEntry("R3 R4 R5"));
        choseData.set(REGISTER_ALU_REGS, new DataStringEntry("R3 R4 R6"));
        rData.set(REGISTER_REGISTERS, choseData);
        registerController.setData(rData);
        rData = (Data) registerModel.getData();
        choseData = (Data) rData.get("registers");
        assertEquals("R1 R2 R3", ((DataStringEntry) (choseData.get(REGISTER_MISSING))).getContent());
        assertEquals("R3 R4 R5", ((DataStringEntry) (choseData.get(REGISTER_FOUND))).getContent());
        assertEquals("R3 R4 R6", ((DataStringEntry) (choseData.get(REGISTER_ALU_REGS))).getContent());
    }

}
