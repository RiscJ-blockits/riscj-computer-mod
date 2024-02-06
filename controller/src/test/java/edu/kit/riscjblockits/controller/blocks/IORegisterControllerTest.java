package edu.kit.riscjblockits.controller.blocks;

import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.Test;

import static edu.kit.riscjblockits.model.blocks.IORegisterModel.REDSTONE_INPUT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IORegisterControllerTest {

    @Test
    void getValue() {
        IORegisterController controller = new IORegisterController(new ArchiCheckStub_Entity(), true, false, REDSTONE_INPUT );
        Value testValue = Value.fromDecimal("5", 3);
        controller.setNewValue(testValue);
        assertEquals(new Value(), controller.getValue());
    }

    @Test
    void setNewValue() {
        IORegisterController controller = new IORegisterController(new ArchiCheckStub_Entity(), false, true, REDSTONE_INPUT );
        assertEquals(new Value(), controller.getValue());
    }

}