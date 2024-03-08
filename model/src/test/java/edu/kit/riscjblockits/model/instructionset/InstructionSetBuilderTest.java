package edu.kit.riscjblockits.model.instructionset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


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

    @Test
    void testFaultyInstructionSet(){
        String ist = "{";
        assertThrows(InstructionBuildException.class, () -> InstructionSetBuilder.buildInstructionSetModel(ist));
    }

    @Test
    void testEmptyInstructionSet(){
        String ist = "";
        assertThrows(InstructionBuildException.class, () -> InstructionSetBuilder.buildInstructionSetModel(ist));
    }

    @Test
    void testBuildInstructionSetModelFromString() {
        String ist = "{\n" +
                "  //FixMe: some micro Instructions are still producing null elements\n" +
                "\n" +
                "  \"name\": \"Test\",             // declare name\n" +
                "  \"instruction_length\": 4,   // declade instruction length\n" +
                "\n" +
                "  \"registers\": {                  // define existing (required) registers\n" +
                "    \"program_counter\": \"fetch1\",   // register containing the current program address\n" +
                "    \"alu\": [                    // alu specific registers\n" +
                "    ],\n" +
                "    \"float\": {},            // float registers\n" +
                "    \"integer\": {            //\n" +
                "      \"test1\": 1,\n" +
                "      \"test2\": 2,\n" +
                "      \"fetch1\": 3,\n" +
                "      \"fetch2\": 4\n" +
                "    },\n" +
                "    \"initial_values\": {\n" +
                "    }\n" +
                "  },\n" +
                "  \"memory\": {\n" +
                "    \"word_length\": 4,\n" +
                "    \"address_length\": 4,\n" +
                "    \"access_delay\": 3,\n" +
                "    \"byte_order\": \"le\",\n" +
                "    \"possible_opcode_lengths\": [4],\n" +
                "    \"opcode_position\": \"MOST\"\n" +
                "  },\n" +
                "  \"alu_operations\": [\n" +
                "  ],\n" +
                "  \"fetch\": [\n" +
                "    [\"fetch1\", \"fetch2\", \"r\", \"<mem_vis>\", \"SAR\"]\n" +
                "  ],\n" +
                "  \"address_change\": {\n" +
                "  },\n" +
                "  \"program_start_label\":\n" +
                "  \"START\",\n" +
                "  \"data_storage_keywords\": {\n" +
                "  },\n" +
                "  \"instructions\": {\n" +
                "    \"TEST\": {\n" +
                "      \"arguments\": [\"\"],\n" +
                "      \"opcode\": \"0000\",\n" +
                "      \"execution\": [\n" +
                "        [\"test1\", \"test2\", \"\", \"\", \"\"],\n" +
                "        [\"test1\", \"test2\", \"\", \"\", \"\"],\n" +
                "        [\"PAUSE\", \"\", \"\", \"\", \"\", \"\", \"\"]\n" +
                "      ],\n" +
                "      \"translation\": [\n" +
                "        \"0000\"\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        try {
            model = InstructionSetBuilder.buildInstructionSetModel(ist);
        } catch (InstructionBuildException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Test", model.getName());
        String[] aluRegisters = {"X", "Y", "Z"};
        HashMap<String, Integer> registers = new HashMap<>();
        registers.put("test1", 1);
        registers.put("test2", 2);
        for (String key :
                registers.keySet()) {
            assertEquals(registers.get(key), model.getIntegerRegister(key));
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