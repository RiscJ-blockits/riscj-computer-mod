package edu.kit.riscjblockits.model.instructionset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstructionSetModelTest {

    InstructionSetModel model;

    @BeforeEach
    void setUp() {
        model = InstructionSetBuilder.buildInstructionSetModel(getClass().getClassLoader().getResourceAsStream("instructionSetMIMA.jsonc"));
    }


    @Test
    void generateHashMaps() {
        model.generateHashMaps();
        assertEquals(1, model.getIntegerRegister("IR"));
        assertEquals(2, model.getIntegerRegister("EINS"));
        assertEquals(3, model.getIntegerRegister("AKKU"));
        assertEquals(4, model.getIntegerRegister("SAR"));
        assertEquals(5, model.getIntegerRegister("SDR"));
    }

    @Test
    void getAluRegisters() {
        assertArrayEquals(new String[]{"X", "Y", "Z"}, model.getAluRegisters());
    }

    @Test
    void getName() {
        assertEquals("MIMA", model.getName());
    }

    @Test
    void getIntegerRegister() {
        assertEquals(1, model.getIntegerRegister("IR"));
    }

    @Test
    void getFloatRegister() {
        assertNull(model.getFloatRegister("F1"));
    }

    @Test
    void getProgramCounter() {
        assertEquals("IAR", model.getProgramCounter());
    }

    @Test
    void getMemoryWordSizeAsByte() {
        assertEquals(3, model.getMemoryWordSize());
    }

    @Test
    void getMemoryAddressSizeAsByte() {
        assertEquals(3, model.getMemoryAddressSize());
    }

    @Test
    void isAddressChange() {
        assertTrue(model.isAddressChange("* = 0x5"));
    }

    @Test
    void isAddressChangeFalse() {
        assertFalse(model.isAddressChange("* 0x5"));
    }

    @Test
    void getChangedAddress() {
        assertEquals("0x5", model.getChangedAddress("* = 0x5"));
    }

    @Test
    void getProgramStartLabel() {
        assertEquals("START", model.getProgramStartLabel());
    }

    @Test
    void isDataStorageCommand() {
        assertTrue(model.isDataStorageCommand("DS 0x5"));
    }

    @Test
    void getStorageCommandData() {
        assertEquals("0x5~24", model.getStorageCommandData("DS 0x5"));
    }

    @Test
    void getFetchPhaseLength() {
        assertEquals(6, model.getFetchPhaseLength());
    }

    @Test
    void getFetchPhaseStep() {
        assertEquals("SAR", model.getFetchPhaseStep(0).getTo());
    }

    @Test
    void getInstructionFromBinary() {
        assertEquals("AKKU", ((MicroInstruction) model.getInstructionFromBinary("000000000000000000000000").getExecution()[0]).getTo());
    }

    @Test
    void getRegisterNames() {
        assertArrayEquals(new String[]{"X", "Y", "Z", "IAR", "SDR", "SAR", "IR", "EINS", "AKKU"}, model.getRegisterNames().toArray());
    }

    @Test
    void getRegisterInitialValue() {
        assertEquals("01", model.getRegisterInitialValue("EINS"));
    }

    @Test
    void getPossibleInstructions() {
        assertArrayEquals(new String[][]{{"HALT", ""}, {"ADD", "[addr]"}, {"EQL", "[addr]"}, {"RAR", ""}, {"OR", "[addr]"},
                {"LDC", "[const]"}, {"JMN", "[addr]"}, {"JMP", "[addr]"}, {"NOT", ""}, {"STV", "[addr]"}, {"AND", "[addr]"}, {"XOR", "[addr]"}, {"LDV", "[addr]"}}, model.getPossibleInstructions().toArray());

    }

    @Test
    void testEquals() {
        InstructionSetModel model1 = InstructionSetBuilder.buildInstructionSetModel(getClass().getClassLoader().getResourceAsStream("instructionSetMIMA.jsonc"));
        assertEquals(model, model1);
    }

    @Test
    void testHashCode() {
        assertEquals(model.hashCode(), model.hashCode());
    }
}