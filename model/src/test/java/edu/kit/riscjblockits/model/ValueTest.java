package edu.kit.riscjblockits.model;

import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueTest {

    @Test
    void emptyValue() {
        Value val = new Value(new byte[0]);
        assertEquals("", val.getBinaryValue());
        assertEquals("", val.getHexadecimalValue());
    }

    @Test
    void getBinaryValue() {
        Value val = new Value(new byte[1]);
        assertEquals("00000000", val.getBinaryValue());

        val = new Value(new byte[]{1});
        assertEquals("00000001", val.getBinaryValue());

        val = new Value(new byte[]{(byte) 128});
        assertEquals("10000000", val.getBinaryValue());

        val = new Value(new byte[]{(byte) 255, 16});
        assertEquals("1111111100010000", val.getBinaryValue());

        val = new Value(new byte[]{(byte) 127, 16});
        assertEquals("0111111100010000", val.getBinaryValue());
    }

    @Test
    void getHexadecimalValue() {
        Value val = new Value(new byte[1]);
        assertEquals("00", val.getHexadecimalValue());

        val = new Value(new byte[]{0, 16});
        assertEquals("0010", val.getHexadecimalValue());

        val = new Value(new byte[]{(byte) 255, 0});
        assertEquals("FF00", val.getHexadecimalValue());
    }

    @Test
    void fromHex() {
        Value val = Value.fromHex("0011", 2);
        assertEquals("0011", val.getHexadecimalValue());

        val = Value.fromHex("11", 2);
        assertEquals("0011", val.getHexadecimalValue());

        val = Value.fromHex("000001", 3);
        assertEquals("000001", val.getHexadecimalValue());

        val = Value.fromHex("FF", 1);
        assertEquals("FF", val.getHexadecimalValue());
    }

    @Test
    void fromHexUnevenLength() {
        Value val = Value.fromHex("1", 1);
        assertEquals("01", val.getHexadecimalValue());

        val = Value.fromHex("FFF", 2);
        assertEquals("0FFF", val.getHexadecimalValue());

    }

    @Test
    void fromBinary() {
        Value val = Value.fromBinary("00000011", 1);
        assertEquals("00000011", val.getBinaryValue());

        val = Value.fromBinary("011", 1);
        assertEquals("00000011", val.getBinaryValue());

        val = Value.fromBinary("10000001", 1);
        assertEquals("10000001", val.getBinaryValue());

        val = Value.fromBinary("11111111", 1);
        assertEquals("FF", val.getHexadecimalValue());

        val = Value.fromBinary("011", 2);
        assertEquals("0000000000000011", val.getBinaryValue());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> Value.fromBinary("abd", 1));

        String expectedMessage = "Value String must only contain 1 and 0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void fromFloatNegative() {
        Value val = Value.fromFloat("-0.05", 4);
        assertEquals("10111101010011001100110011001101", val.getBinaryValue());
    }

    @Test @Disabled
    void getFloatValue() {
        Value val = Value.fromBinary("00111101010011001100110011001101", 4);
        assertEquals("0.05", val.getFloatValue());
    }

    @Test
    void fromDecimal() {
        Value val = Value.fromDecimal("5", 4);
        assertEquals("00000000000000000000000000000101", val.getBinaryValue());
    }

    @Test
    void fromFloat() {
        Value val = Value.fromFloat("0.0", 4);
        assertEquals("00000000", val.getHexadecimalValue());

        val = Value.fromFloat("2.5", 4);
        assertEquals("40200000", val.getHexadecimalValue());

        val = Value.fromFloat("-1.69", 4);
        assertEquals("BFD851EC", val.getHexadecimalValue());
    }

    @Test
    void getIncrementedValue() {
        Value val = Value.fromHex("00", 1).getIncrementedValue();
        assertEquals("01", val.getHexadecimalValue());

        val = Value.fromHex("FF", 1).getIncrementedValue();
        assertEquals("00", val.getHexadecimalValue());

        val = Value.fromHex("00FF", 2).getIncrementedValue();
        assertEquals("0100", val.getHexadecimalValue());
    }

    @Test
    void testLowerThan() {
        Value val1 = Value.fromHex("F0", 1);
        Value val2 = Value.fromHex("20", 1);
        assertTrue(val1.lowerThan(val2));
        assertFalse(val2.lowerThan(val1));
    }

    @Test
    void testLowerThanUnsigned() {
        Value val1 = Value.fromHex("10", 1);
        Value val2 = Value.fromHex("F0", 1);
        assertTrue(val1.lowerThanUnsigned(val2));
        assertFalse(val2.lowerThanUnsigned(val1));
    }

    @Test
    void testGreaterThan() {
        Value val1 = Value.fromHex("30", 1);
        Value val2 = Value.fromHex("20", 1);
        assertTrue(val1.greaterThan(val2));
        assertFalse(val2.greaterThan(val1));
    }

    @Test
    void testGreaterThanUnsigned() {
        Value val1 = Value.fromHex("F0", 1);
        Value val2 = Value.fromHex("20", 1);
        assertTrue(val1.greaterThanUnsigned(val2));
        assertFalse(val2.greaterThanUnsigned(val1));
    }

    @Test
    void fromDecimalLonger() {
        Value val = Value.fromDecimal("348", 1);
        assertEquals("5C", val.getHexadecimalValue());
    }

    @Test
    void greaterThanFloat() {
        Value val1 = Value.fromFloat("1.0", 4);
        Value val2 = Value.fromFloat("0.0", 4);
        assertTrue(val1.greaterThanFloat(val2));
        assertFalse(val2.greaterThanFloat(val1));
    }

    @Test
    void equals() {
        Value val1 = Value.fromHex("F0", 1);
        Value val2 = Value.fromHex("F0", 1);
        assertEquals(val1, val2);
    }

    @Test
    void testHashCode() {
        Value val1 = Value.fromHex("F0", 1);
        assertEquals(val1.hashCode(), val1.hashCode());
    }

    @Test
    void lowerThanFloat() {
        Value val1 = Value.fromFloat("0.0", 4);
        Value val2 = Value.fromFloat("1.0", 4);
        assertTrue(val1.lowerThanFloat(val2));
        assertFalse(val2.lowerThanFloat(val1));
    }

    @Test
    void lowerThanFloatWeirdValues() {
        Value val1 = Value.fromFloat("0.05", 4);
        Value val2 = Value.fromFloat("0.06", 4);
        assertTrue(val1.lowerThan(val2));
        assertFalse(val2.lowerThan(val1));
    }


    @Test @Disabled
    void greaterThanFloatWeirdValues() {
        Value val1 = Value.fromFloat("0.05", 4);
        Value val2 = Value.fromFloat("0.06", 4);
        assertTrue(val2.greaterThanFloat(val1));
        assertFalse(val1.greaterThanFloat(val2));
    }

    @Test
    void negateInt() {
        Value val = Value.fromHex("F0", 1).negate();
        assertEquals("10", val.getHexadecimalValue());
    }
}