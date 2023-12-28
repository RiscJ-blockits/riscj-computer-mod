package edu.kit.riscjblockits.model;

import java.util.Arrays;
import java.util.HexFormat;

public class Value {
    private final byte[] value;

    public static Value fromHex(String s) {
        return new Value(HexFormat.of().parseHex(s));
    }


    public Value(byte[] initial) {
        this.value = initial;
    }

    /**
     * Null-Constructor for empty value which equals empty byte array.
     */
    public Value() {
        this.value = new byte[0];
    }

    public byte[] getValue() {
        return value;
    }

    public String getBinaryValue() {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte val: value) {
            stringBuilder.append(String.format("%8s", Integer.toBinaryString(val & 0xFF)).replace(' ', '0'));
        }

        return stringBuilder.toString();
    }

    public String getHexadecimalValue() {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte val: value) {
            stringBuilder.append(String.format("%02X", val));
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value value1 = (Value) o;
        return Arrays.equals(value, value1.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }
}
