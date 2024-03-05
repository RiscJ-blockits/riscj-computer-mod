package edu.kit.riscjblockits.model.instructionset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InstructionSetRegistersTest {

    HashMap<String, Integer> floatRegs = new HashMap<>();
    HashMap<String, Integer> intRegs = new HashMap<>();
    HashMap<String, String> initialValues = new HashMap<>();
    InstructionSetRegisters test;

    @BeforeEach
    void setUp() {
        floatRegs.put("F1", 1);
        floatRegs.put("F2", 2);
        intRegs.put("R1", 1);
        intRegs.put("R2", 2);
        initialValues.put("R1", "0");
        test = new InstructionSetRegisters("PC", new String[]{"A1", "A2"}, floatRegs, intRegs, initialValues);
    }

    @Test
    void generateRegisterAddressMaps() {
        test.generateRegisterAddressMaps();
        assertEquals("R1", test.getIntRegisterAddressMap().get(1));
        assertEquals("R2", test.getIntRegisterAddressMap().get(2));
        assertEquals("F1", test.getFloatRegisterAddressMap().get(1));
        assertEquals("F2", test.getFloatRegisterAddressMap().get(2));
    }

    @Test
    void getFloatRegister() {
        assertEquals(1, test.getFloatRegister("F1"));
    }

    @Test
    void getIntegerRegister() {
        assertEquals(1, test.getIntegerRegister("R1"));
    }

    @Test
    void getProgramCounter() {
        assertEquals("PC", test.getProgramCounter());
    }

    @Test
    void getAluRegs() {
        assertArrayEquals(new String[]{"A1", "A2"}, test.getAluRegs());
    }

    @Test
    void getRegisterNames() {
        assertArrayEquals(new String[]{"A1", "A2", "PC", "R2", "R1", "F1", "F2"}, test.getRegisterNames().toArray());
    }

    @Test
    void getInitialValue() {
        assertEquals("0", test.getInitialValue("R1"));
    }

    @Test
    void getIntRegisterAddressMap() {
        test.generateRegisterAddressMaps();
        HashMap<Integer, String> intRegsGet =  test.getIntRegisterAddressMap();
        assertEquals("R1", intRegsGet.get(1));
    }

    @Test
    void getFloatRegisterAddressMap() {
        test.generateRegisterAddressMaps();
        HashMap<Integer, String> floatRegsGet =  test.getFloatRegisterAddressMap();
        assertEquals("F1", floatRegsGet.get(1));
    }
}