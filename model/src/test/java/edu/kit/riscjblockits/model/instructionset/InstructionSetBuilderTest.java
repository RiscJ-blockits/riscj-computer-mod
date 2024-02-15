package edu.kit.riscjblockits.model.instructionset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


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
        }  catch (InstructionBuildException e) {
            throw new RuntimeException(e);
        }
    }

    public static InstructionSetModel buildInstructionSetModelMima() {
        InputStream is = InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetMIMA.jsonc");
        try {
            return InstructionSetBuilder.buildInstructionSetModel(is);
        }  catch (InstructionBuildException e) {
            throw new RuntimeException(e);
        }
    }

    public static InstructionSetModel buildInstructionSetModelRiscV() {
        InputStream is = InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetRiscV.jsonc");
        try {
            return InstructionSetBuilder.buildInstructionSetModel(is);
        }  catch (InstructionBuildException e) {
            throw new RuntimeException(e);
        }
    }

}