package edu.kit.riscjblockits.controller.assembler;

import edu.kit.riscjblockits.model.data.IDataContainer;
import edu.kit.riscjblockits.model.instructionset.InstructionBuildException;
import edu.kit.riscjblockits.model.instructionset.InstructionSetBuilder;
import edu.kit.riscjblockits.model.instructionset.InstructionSetModel;
import edu.kit.riscjblockits.model.memoryrepresentation.Memory;
import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class AssemblerTest {

    private static InstructionSetModel buildInstructionSetModelMima() {
        InputStream is = InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetMIMA.jsonc");
        try {
            return InstructionSetBuilder.buildInstructionSetModel(is);
        }  catch (InstructionBuildException e) {
            throw new RuntimeException(e);
        }
    }

    private static InstructionSetModel buildInstructionSetModelRiscV() {
        InputStream is = InstructionSetBuilder.class.getClassLoader().getResourceAsStream("instructionSetRiscV.jsonc");
        try {
            return InstructionSetBuilder.buildInstructionSetModel(is);
        }  catch (InstructionBuildException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * tests the assembler, expected results are from MIMA flux
     *
     * @throws AssemblyException
    */
    @Test
    void assemble() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelMima();
        Assembler assembler = new Assembler(model);
        assembler.assemble("\n\nADD 0x16");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());

        Value val = memory.getValueAt(Value.fromHex("00", 3));
        assertEquals("300016", val.getHexadecimalValue());

        assembler = new Assembler(model);
        assembler.assemble("""



                ADD 0x16
                test: STV 5
                * = 4
                JMP test
                HALT
                   LDV LABEL

                   *= 8
                LABEL: HALT""");
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
        InstructionSetModel model = buildInstructionSetModelMima();
        Assembler assembler = new Assembler(model);

        assembler.assemble("DS 0x1234");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 3));
        assertEquals("001234", val.getHexadecimalValue());
    }


    @Test
    void testEmptyLabel() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelMima();
        Assembler assembler = new Assembler(model);
        assembler.assemble("*=16\ntest: \n ADD 0x16\n * = 0\n JMP test");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 3));
        assertEquals("800010", val.getHexadecimalValue());
    }


    @Test
    void testComment() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelMima();
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
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("addi t1, t2, 0xFF");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("0FF38313", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscSW() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("sw t1, 16(t2)");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("0063A823", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscSLLI() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("slli t1, t2, 0x12");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("01239313", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscSLL() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("sll t1, t2, t3");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("01C39333", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscLUI() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("lui t1, 0x021234");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("21234337", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscJAL() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("jal t1, 0x1234");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("4680236F", val.getHexadecimalValue());    // difference to Rars as we are using word addressing
    }
    @Test
    void assembleRiscBGE() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("bge, s0, t0, 0x04");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("00545363", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscADDINegValue() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble("addi, t0, t0, -4");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("FFC28293", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscDataWord() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble(".word 0x12345678");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("12345678", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscDataStringSingleChar() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble(".ascii \"a\"");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("00000061", val.getHexadecimalValue());
    }

    @Test
    void assembleRiscDataStringMultiChar() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble(".ascii \"hello\"");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        Value val = memory.getValueAt(Value.fromHex("00", 4));
        assertEquals("00000068", val.getHexadecimalValue());

        val = memory.getValueAt(Value.fromHex("01", 4));
        assertEquals("00000065", val.getHexadecimalValue());

        val = memory.getValueAt(Value.fromHex("02", 4));
        assertEquals("0000006C", val.getHexadecimalValue());

        val = memory.getValueAt(Value.fromHex("03", 4));
        assertEquals("0000006C", val.getHexadecimalValue());

        val = memory.getValueAt(Value.fromHex("04", 4));
        assertEquals("0000006F", val.getHexadecimalValue());
    }

    @Test
    void invalidInstruction() {
        InstructionSetModel model = buildInstructionSetModelMima();
        Assembler assembler = new Assembler(model);
        assertThrowsExactly(AssemblyException.class, () -> assembler.assemble("AD 0x16"));
    }

    @Test
    void invalidLabel() {
        InstructionSetModel model = buildInstructionSetModelMima();
        Assembler assembler = new Assembler(model);
        assertThrowsExactly(AssemblyException.class, () -> assembler.assemble("ADD x"));
    }

    @Test
    void invalidLine() {
        InstructionSetModel model = buildInstructionSetModelMima();
        Assembler assembler = new Assembler(model);
        assertThrowsExactly(AssemblyException.class, () -> assembler.assemble("dxdr_: h~ooll"));
    }

    @Test
    void floatRegisterInsert() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);
        assembler.assemble("flw ft1, 0(t1)");
    }

    @Test
    void assembleProgramStartLabel() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);
        assembler.assemble("add t1, t2, t3\nmain: addi t1, t2, 0xFF");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        assertEquals("00000001", memory.getInitialProgramCounter().getHexadecimalValue());
    }

    @Test
    void assembleAsciiStartPos() throws AssemblyException {
        InstructionSetModel model = buildInstructionSetModelRiscV();
        Assembler assembler = new Assembler(model);

        assembler.assemble(".ascii \"hello\"\nmain: addi t1, t2, 0xFF");
        Memory memory = Memory.fromData((IDataContainer) assembler.getMemoryData());
        assertEquals("00000005", memory.getInitialProgramCounter().getHexadecimalValue());
    }
}