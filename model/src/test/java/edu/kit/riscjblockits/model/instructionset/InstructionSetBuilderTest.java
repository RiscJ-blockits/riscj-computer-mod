package edu.kit.riscjblockits.model.instructionset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InstructionSetBuilderTest {

    InstructionSetModel model;
    @Test
    void testHashMapExtraction() {
        HashMap<String, Integer> registers = new HashMap<>();
        registers.put("IR", 1);
        registers.put("SDR", 5);
        for (String key :
            registers.keySet()) {
            assertEquals(registers.get(key), model.getIntegerRegister(key));
        }
    }

    @Test
    void testName() {
        assertEquals("Test", model.getName());
    }

    @Test
    void testArrayExtraction() {
        String[] aluRegisters = {"X", "Y", "Z"};
        assertArrayEquals(aluRegisters, model.getAluRegisters());
    }

    @BeforeEach
    void pre() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("instructionSetTEST.jsonc");
        try {
            model = InstructionSetBuilder.buildInstructionSetModel(is);
        }  catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}