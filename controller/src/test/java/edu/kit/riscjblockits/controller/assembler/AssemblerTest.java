package edu.kit.riscjblockits.controller.assembler;

import edu.kit.riscjblockits.model.Memory;
import edu.kit.riscjblockits.model.Value;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssemblerTest {

    /*/**
     * tests the assembler, expected results are from MIMA flux
     *
     * @throws AssemblyException

    @Disabled
    @Test
    void assemble() throws AssemblyException {
        InstructionSetModel model = InstructionSetBuilder.buildInstructionSetModelMima();
        Assembler assembler = new Assembler(model);
        assembler.assemble("ADD 0x16");
        Memory memory = assembler.getMemory();
        Value val = memory.getValueAt(Value.fromHex("00", 3));
        assertEquals("300016", val.getHexadecimalValue());

        assembler = new Assembler(model);
        assembler.assemble( "\n" +
                                        "\n" +
                                        "\n" +
                                        "ADD 0x16\n" +
                                        "test: STV 5\n" +
                                        "* = 4\n" +
                                        "JMP test\n" +
                                        "HALT\n" +
                                        "   LDV LABEL\n" +
                                        "\n   " +
                                        "*= 8\n" +
                                        "LABEL: HALT");
        memory = assembler.getMemory();
        val = memory.getValueAt(Value.fromHex("00", 3));
        assertEquals("300016", val.getHexadecimalValue());
        val = memory.getValueAt(Value.fromHex("01", 3));
        assertEquals("200005", val.getHexadecimalValue());
        val = memory.getValueAt(Value.fromHex("03", 3));
        assertEquals("000000", val.getHexadecimalValue());
        val = memory.getValueAt(Value.fromHex("04", 3));
        assertEquals("800001", val.getHexadecimalValue());
        val = memory.getValueAt(Value.fromHex("05", 3));
        assertEquals("F00000", val.getHexadecimalValue());

        val = memory.getValueAt(Value.fromHex("06", 3));
        assertEquals("100008", val.getHexadecimalValue());
        val = memory.getValueAt(Value.fromHex("08", 3));
        assertEquals("F00000", val.getHexadecimalValue());
    }
    */
}