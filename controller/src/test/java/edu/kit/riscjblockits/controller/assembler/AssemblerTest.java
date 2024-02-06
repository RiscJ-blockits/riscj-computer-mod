package edu.kit.riscjblockits.controller.assembler;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssemblerTest {

    /**
     * tests the assembler, expected results are from MIMA flux
     *
     * @throws AssemblyException
    */
    @Test
    void assemble() throws AssemblyException {
        InstructionSetModel model = InstructionSetBuilder.buildInstructionSetModelMima();
        Assembler assembler = new Assembler(model);
        assembler.assemble("\n\nADD 0x16");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
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
        memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
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

    @Test
    void assembleMIMADataChange() throws AssemblyException {
        InstructionSetModel model = InstructionSetBuilder.buildInstructionSetModelMima();
        Assembler assembler = new Assembler(model);

        assembler.assemble("DS 0x1234");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 3));
        assertEquals("001234", val.getHexadecimalValue());
    }


    @Test
    void testEmptyLabel() throws AssemblyException {
        InstructionSetModel model = InstructionSetBuilder.buildInstructionSetModelMima();
        Assembler assembler = new Assembler(model);
        assembler.assemble("*=16\ntest: \n ADD 0x16\n * = 0\n JMP test");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 3));
        assertEquals("800010", val.getHexadecimalValue());
    }


    @Test
    void testComment() throws AssemblyException {
        InstructionSetModel model = InstructionSetBuilder.buildInstructionSetModelMima();
        Assembler assembler = new Assembler(model);
        assembler.assemble("ADD 0x16 #comment\n ADD 0x17 ;comment");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 3));
        assertEquals("300016", val.getHexadecimalValue());

        val = memory.getValueAt(Value.fromHex("01", 3));
        assertEquals("300017", val.getHexadecimalValue());
    }

    /**
     * tests the assembler, expected results are from RARS
     *
     * @throws AssemblyException
     */
    @Test
    void assembleRisc() throws AssemblyException {
        InstructionSetModel model = InstructionSetBuilder.buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("addi t1, t2, 0xFF");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("0FF38313", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscSW() throws AssemblyException {
        InstructionSetModel model = InstructionSetBuilder.buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("sw t1, 16(t2)");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("0063A823", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscSLLI() throws AssemblyException {
        InstructionSetModel model = InstructionSetBuilder.buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("slli t1, t2, 0x12");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("01239313", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscSLL() throws AssemblyException {
        InstructionSetModel model = InstructionSetBuilder.buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("sll t1, t2, t3");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("01C39333", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscLUI() throws AssemblyException {
        InstructionSetModel model = InstructionSetBuilder.buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("lui t1, 0x021234");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("21234337", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscJAL() throws AssemblyException {
        InstructionSetModel model = InstructionSetBuilder.buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("jal t1, 0x1234");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("2340136F", val.getHexadecimalValue());
    }
    // TODO B type

}