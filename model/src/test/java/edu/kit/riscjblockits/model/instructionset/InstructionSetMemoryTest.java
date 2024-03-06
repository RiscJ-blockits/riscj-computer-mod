package edu.kit.riscjblockits.model.instructionset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InstructionSetMemoryTest {

    InstructionSetMemory test;

    @BeforeEach
    void setUp() {
        test = new InstructionSetMemory(32, 32, 1, "big", List.of(4, 8), "most");
    }

    @Test
    void getWordSize() {
        assertEquals(32, test.getWordSize());
    }

    @Test
    void getAddressSize() {
        assertEquals(32, test.getAddressSize());
    }

    @Test
    void getAccessDelay() {
        assertEquals(1, test.getAccessDelay());
    }

    @Test
    void getByteOrder() {
        assertEquals("big", test.getByteOrder());
    }

    @Test
    void getPossibleOpcodeLengths() {
        assertArrayEquals(new Integer[]{4, 8}, test.getPossibleOpcodeLengths().toArray());
    }

    @Test
    void getOpcodePosition() {
        assertEquals("most", test.getOpcodePosition());
    }
}