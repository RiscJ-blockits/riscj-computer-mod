package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegisterControllerTest {

    private RegisterController registerController;

    @BeforeEach
    void init() {
        registerController = new RegisterController(new ArchiCheckStub_Entity());
    }

    @Test
    void setData() {
        Data choseData = new Data();
        choseData.set("missing", new DataStringEntry("R1 R2 R3"));
        choseData.set("found", new DataStringEntry("R3 R4 R5"));
        Data rData = new Data();
        rData.set("registers", choseData);
        rData.set("word", new DataStringEntry("32"));
        rData.set("type", new DataStringEntry("IR"));
        registerController.setData(rData);
        RegisterModel registerModel = (RegisterModel) registerController.getModel();
        rData = (Data) registerModel.getData();
        assertEquals("32", ((DataStringEntry) (rData.get("word"))).getContent());
        assertEquals("IR", ((DataStringEntry) (rData.get("type"))).getContent());
        choseData = (Data) rData.get("registers");
        assertEquals("R1 R2 R3", ((DataStringEntry) (choseData.get("missing"))).getContent());
        assertEquals("R3 R4 R5", ((DataStringEntry) (choseData.get("found"))).getContent());
    }

}