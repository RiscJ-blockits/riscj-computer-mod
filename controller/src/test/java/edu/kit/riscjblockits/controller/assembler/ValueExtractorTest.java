package edu.kit.riscjblockits.controller.assembler;

import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ValueExtractorTest {

    @Test
    void extractValueDecimal() {
        assertEquals(Value.fromDecimal("123", 4), ValueExtractor.extractValue("123", 4));
        assertEquals(Value.fromDecimal("123", 4).negate(), ValueExtractor.extractValue("-123", 4));
    }

    @Test
    void extractValueHex() {
        assertEquals(Value.fromHex("0123", 4), ValueExtractor.extractValue("0x0123", 4));
        assertEquals(Value.fromHex("0123", 4).negate(), ValueExtractor.extractValue("-0x0123", 4));
    }

    @Test
    void extractValueBinary() {
        assertEquals(Value.fromBinary("1010", 4), ValueExtractor.extractValue("0b1010", 4));
        assertEquals(Value.fromBinary("1010", 4).negate(), ValueExtractor.extractValue("-0b1010", 4));
    }

    @Test
    void extractValueInvalid() {
        assertNull(ValueExtractor.extractValue("0xg", 4));
        assertNull(ValueExtractor.extractValue("0b2", 4));
        assertNull(ValueExtractor.extractValue("123a", 4));
    }
}