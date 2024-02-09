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
        byte[] hexBytes = HexFormat.of().parseHex(s); //Todo fill with zeros if length is not a multiple of 2
        int offset = length - hexBytes.length;
        offset = Math.max(offset, 0);
        int endOffset = Math.max(hexBytes.length - length, 0);
        for (int i = 0; i < hexBytes.length - endOffset; i++) {
            bytes[i + offset] = hexBytes[i + endOffset];
        }

        return new Value(bytes);
    }

    /**
     * Creates a value from a binary string
     * @param s the binary string
     * @param length the length of the value in bytes
     * @return the value
     */
    public static Value fromBinary(String s, int length, boolean autoSignExtend) {
        byte[] bytes = new byte[length];
        int currentByte = 0;
        int currentBit = 0;

        int missingBits = length * 8 - s.length();
        boolean signExtend = autoSignExtend && s.charAt(0) == '1';
        for (int i = 0; i < length * 8; i++){
            char c;
            // missing bits are filled with 0 (1 if sign extend is true)
            if (i < missingBits) {
                c = signExtend ? '1' : '0';
            } else {
                c = s.charAt(i - missingBits);
            }

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

    public static Value fromBinary(String s, int length) {
        return fromBinary(s, length, false);
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

    public boolean lowerThan(Value comparator) {
        BigInteger thisValue = new BigInteger(this.getByteValue());
        BigInteger comparatorValue = new BigInteger(comparator.getByteValue());
        return thisValue.compareTo(comparatorValue) < 0;
    }

    public boolean lowerThanUnsigned(Value comparator) {;
        byte[] valueUnsigned = new byte[value.length + 1];
        System.arraycopy(value, 0, valueUnsigned, 1, value.length);
        byte[] compUnsigned = new byte[value.length + 1];
        System.arraycopy(comparator.getByteValue(), 0, compUnsigned, 1, comparator.getByteValue().length);
        BigInteger thisValue = new BigInteger(valueUnsigned);
        BigInteger comparatorValue = new BigInteger(compUnsigned);
        return thisValue.compareTo(comparatorValue) < 0;
    }

    public boolean lowerThanFloat(Value comparator) {
        //TODO implement
        return false;
    }

    public boolean greaterThan(Value comparator) {
        BigInteger thisValue = new BigInteger(this.getByteValue());
        BigInteger comparatorValue = new BigInteger(comparator.getByteValue());
        return thisValue.compareTo(comparatorValue) > 0;
    }

    public boolean greaterThanUnsigned(Value comparator) {
        byte[] valueUnsigned = new byte[value.length + 1];
        System.arraycopy(value, 0, valueUnsigned, 1, value.length);
        byte[] compUnsigned = new byte[value.length + 1];
        System.arraycopy(comparator.getByteValue(), 0, compUnsigned, 1, comparator.getByteValue().length);
        BigInteger thisValue = new BigInteger(valueUnsigned);
        BigInteger comparatorValue = new BigInteger(compUnsigned);
        return thisValue.compareTo(comparatorValue) > 0;
    }

    public boolean greaterThanFloat(Value comparator) {
        //TODO implement
        return false;
    }

    public Value negate() {
        // invert
        for (int i = 0; i < value.length; i++) {
            value[i] = (byte) ~value[i];
        }
        // increment to get two's complement
        value[value.length - 1]++;
        return this;
    }
}
