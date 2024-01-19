package edu.kit.riscjblockits.model;

import edu.kit.riscjblockits.model.memoryrepresentation.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueTest {

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
    void fromBinary() {
        Value val = Value.fromBinary("00000011", 1);
        assertEquals("00000011", val.getBinaryValue());

        val = Value.fromBinary("011", 1);
        assertEquals("00000011", val.getBinaryValue());

        val = Value.fromBinary("10000001", 1);
        assertEquals("10000001", val.getBinaryValue());

        val = Value.fromBinary("11111111", 1);
        assertEquals("FF", val.getHexadecimalValue());
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
}