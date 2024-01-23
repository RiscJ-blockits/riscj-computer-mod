package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.blocks.AluModel;
import edu.kit.riscjblockits.model.blocks.RegisterModel;
import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.DataStringEntry;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AluControllerTest {

    private AluController aluController;

    @BeforeEach
    void init() {
        aluController = new AluController(new ArchiCheckStub_Entity());
    }

    @Test
    void setData() {
        Data aluData = new Data();
        aluData.set("operation", new DataStringEntry("add"));
        aluController.setData(aluData);
        AluModel aluModel = (AluModel) aluController.getModel();
        aluData = (Data) aluModel.getData();
        assertEquals("add", ((IDataStringEntry) (aluData.get("operation"))).getContent());
    }

}