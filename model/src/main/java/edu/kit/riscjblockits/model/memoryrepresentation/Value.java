package edu.kit.riscjblockits.model.memoryrepresentation;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HexFormat;

/**
 * This class represents a value and
 * also provides methods to convert the value to and from binary and hexadecimal
 * as well as incrementing the value.
 * Holds data for handling between Controller and Model.
 */
public class Value {

    /**
     * the value as byte array
     */
    private final byte[] value;

    /**
     * Creates a value from a hexadecimal string
     * @param s the hexadecimal string
     * @param length the length of the value in bytes
     * @return the value
     */
    public static Value fromHex(String s, int length) {
        byte[] bytes = new byte[length];
        byte[] hexBytes = HexFormat.of().parseHex(s);
        int offset = length - hexBytes.length;
        for (int i = 0; i < hexBytes.length; i++) {
            bytes[i + offset] = hexBytes[i];
        }

        return new Value(bytes);
    }

    /**
     * Creates a value from a binary string
     * @param s the binary string
     * @param length the length of the value in bytes
     * @return the value
     */
    public static Value fromBinary(String s, int length) {
        byte[] bytes = new byte[length];
        int currentByte = 0;
        int currentBit = 0;
        for (char c: s.toCharArray()) {
            if (c == '1') {
                bytes[currentByte] = (byte) (bytes[currentByte] << 1);
                bytes[currentByte] = (byte) (bytes[currentByte] | 1);
            } else if (c == '0') {
                bytes[currentByte] = (byte) (bytes[currentByte] << 1);
            } else {
                throw new IllegalArgumentException("Value String must only contain 1 and 0");
            }
            currentBit++;
            if (currentBit == 8) {
                currentBit = 0;
                currentByte++;
            }
        }

        return new Value(bytes);
    }

    /**
     * Creates a value from a floating point decimal string
     * @param s the float string
     * @param length the length of the value in bytes
     * @return the value
     */
    public static Value fromFloat(String s, int length) {
        return null;
    }

    /**
     * Constructor for a value
     * @param initial the initial value as byte array
     */
    public Value(byte[] initial) {
        this.value = initial;
    }

    /**
     * Null-Constructor for empty value which equals empty byte array.
     */
    public Value() {
        this.value = new byte[0];
    }

    public static Value fromDecimal(String value, int length) {
        byte[] val = new BigInteger(value).toByteArray();
        byte[] bytes = new byte[length];
        if (val.length > length) {
            System.arraycopy(val, val.length - length, bytes, 0, length);
        } else {
            System.arraycopy(val, 0, bytes, length - val.length, val.length);
        }
        return new Value(bytes);
    }

    /**
     * @return the value as byte array
     */
    public byte[] getByteValue() {
        return value;
    }

    /**
     * @return the value as binary string
     */
    public String getBinaryValue() {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte val: value) {
            stringBuilder.append(String.format("%8s", Integer.toBinaryString(val & 0xFF)).replace(' ', '0'));
        }

        return stringBuilder.toString();
    }

    /**
     * @return the value as floating point decimal string
     */
    public String getFloatValue() {
        //ToDo
        return null;
    }

    /**
     * @return the value as hexadecimal string
     */
    public String getHexadecimalValue() {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte val: value) {
            stringBuilder.append(String.format("%02X", val));
        }

        return stringBuilder.toString();
    }

    /**
     * return the value incremented by 1
     * @return the incremented value
     */
    public Value getIncrementedValue() {
        byte[] incrementedValue = new byte[value.length];
        System.arraycopy(value, 0, incrementedValue, 0, value.length);

        for (int i = incrementedValue.length - 1; i >= 0; i--) {
            // if value is -1 (0xFF) set to 0
            if (incrementedValue[i] == -1) {
                incrementedValue[i] = 0;
            } else {
                incrementedValue[i]++;
                break;
            }
        }
        return new Value(incrementedValue);
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

    @Override
    public String toString() {
        return getHexadecimalValue();
    }

}
