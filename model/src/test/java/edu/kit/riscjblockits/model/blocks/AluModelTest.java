package edu.kit.riscjblockits.model.blocks;

import edu.kit.riscjblockits.model.data.Data;
import edu.kit.riscjblockits.model.data.IDataStringEntry;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static edu.kit.riscjblockits.model.data.DataConstants.ALU_OPERAND1;
import static edu.kit.riscjblockits.model.data.DataConstants.ALU_OPERAND2;
import static edu.kit.riscjblockits.model.data.DataConstants.ALU_OPERATION;
import static edu.kit.riscjblockits.model.data.DataConstants.ALU_RESULT;
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

    @Test
    void getData2() {
        aluModel.setOperand1(Value.fromDecimal("49", 3));
        aluModel.setOperand2(Value.fromDecimal("1", 3));
        aluModel.setOperation("add");
        aluModel.setResult(Value.fromDecimal("50", 3));
        Data aluData = (Data) aluModel.getData();
        assertEquals("add", ((IDataStringEntry) (aluData.get(ALU_OPERATION))).getContent());
        assertEquals("000031", ((IDataStringEntry) (aluData.get(ALU_OPERAND1))).getContent());
        assertEquals("000001", ((IDataStringEntry) (aluData.get(ALU_OPERAND2))).getContent());
        assertEquals("000032", ((IDataStringEntry) (aluData.get(ALU_RESULT))).getContent());
    }

}
