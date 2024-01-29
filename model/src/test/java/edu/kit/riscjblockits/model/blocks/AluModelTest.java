package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AluModelTest {

    private AluModel aluModel;

    @BeforeEach
    void init() {
        aluModel = new AluModel();
    }

    @Test
    void getData() {
        aluModel.setOperand1(Value.fromDecimal("49", 5));
        aluModel.setOperation("add");
        Data aluData = (Data) aluModel.getData();
        assertEquals("add", ((IDataStringEntry) (aluData.get("operation"))).getContent());
        assertEquals("0000000031", ((IDataStringEntry) (aluData.get("operand1"))).getContent());
    }

}