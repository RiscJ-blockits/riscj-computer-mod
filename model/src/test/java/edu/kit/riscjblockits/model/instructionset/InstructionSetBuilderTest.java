package edu.kit.riscjblockits.model.instructionset;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InstructionSetBuilderTest {

    InstructionSetModel model;
    @Test
    void testSimpleHashMapExtraction() {
        HashMap<String, Integer> registers = new HashMap<>();
        registers.put("IR", 1);
        registers.put("SDR", 5);
        for (String key :
            registers.keySet()) {
            assertEquals(registers.get(key), model.getIntegerRegisters().get(key));
        }
    }

    void testAdvancedHashMapExtraction() {
        Map<String, Pair<String[], String[]>> commandArgumentsTranslationMap = new HashMap<>();
        commandArgumentsTranslationMap.put("LDC", new Pair<>(
            new String[] {"[const]"}, new String[] {"0000", "[const]<20>"}));

        for (String key :
            commandArgumentsTranslationMap.keySet()) {
            assertEquals(commandArgumentsTranslationMap.get(key).getKey(),
                model.getCommandArgumentsTranslationMap().get(key).getKey());
            assertEquals(commandArgumentsTranslationMap.get(key).getValue(),
                model.getCommandArgumentsTranslationMap().get(key).getValue());
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
        InputStream is = getClass().getClassLoader().getResourceAsStream("instructionSetTEST.json");
        try {
            model = InstructionSetBuilder.buildInstructionSetModel(is);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}