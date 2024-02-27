package edu.kit.riscjblockits.model.memoryrepresentation;

import java.math.BigInteger;
import java.nio.ByteBuffer;
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
     * the value as a byte array.
     */
    private final byte[] value;

    /**
     * Constructor for a value.
     * @param initial the initial value as a byte array
     */
    public Value(byte[] initial) {
        this.value = initial;
    }

    /**
     * Null-Constructor for empty value which equals an empty byte array.
     */
    public Value() {
        this.value = new byte[0];
    }

    /**
     * Creates a value from a hexadecimal string.
     * @param s the hexadecimal string
     * @param length the length of the value in bytes
     * @return the value
     */
    public static Value fromHex(String s, int length) {
        byte[] bytes = new byte[length];
        byte[] hexBytes = HexFormat.of().parseHex("0".repeat(s.length()%2) + s);
        int offset = length - hexBytes.length;
        offset = Math.max(offset, 0);
        int endOffset = Math.max(hexBytes.length - length, 0);
        for (int i = 0; i < hexBytes.length - endOffset; i++) {
            bytes[i + offset] = hexBytes[i + endOffset];
        }

        return new Value(bytes);
    }

    /**
     * Creates a value from a binary string.
     * @param s binary string
     * @param length the length of the value in bytes
     * @param autoSignExtend ToDo
     * @return the value
     * @throws IllegalArgumentException if the string contains characters other than 1 and 0
     */
    public static Value fromBinary(String s, int length, boolean autoSignExtend) {
        byte[] bytes = new byte[length];
        int currentByte = 0;
        int currentBit = 0;

        int missingBits = length * 8 - s.length();
        boolean signExtend = autoSignExtend && s.charAt(0) == '1';
        for (int i = 0; i < length * 8; i++){
            char c;
            // the missing bits are filled with 0 (1 if sign extend is true)
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

    /**
     * Creates a value from a string containing a binary number.
     * @param s the string.
     * @param length the length of the value in bytes
     * @return the new value
     */
    public static Value fromBinary(String s, int length) {
        return fromBinary(s, length, false);
    }

    /**
     * Creates a value from a floating point decimal string.
     * @param s the float string
     * @param length the length of the value in bytes
     * @return the value
     */
    public static Value fromFloat(String s, int length) {
        byte[] bytes = new byte[length];
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putFloat(Float.parseFloat(s));
        System.arraycopy(buffer.array(), 0, bytes, 0, length);
        return new Value(bytes);
    }

    /**
     * Creates a value from a string containing a decimal number.
     * @param value the value as a string
     * @param length the length of the value in bytes
     * @return the new value
     */
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
     * Return the value as a byte array.
     * @return value as a byte array
     */
    public byte[] getByteValue() {
        return value;
    }

    /**
     * Converts the value to a binary string.
     * @return value as binary string
     */
    public String getBinaryValue() {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte val: value) {
            stringBuilder.append(String.format("%8s", Integer.toBinaryString(val & 0xFF)).replace(' ', '0'));
        }
        return stringBuilder.toString();
    }

    /**
     * Converts the value to a float string.
     * @return the value as floating point decimal string
     */
    public String getFloatValue() {
        ByteBuffer wrapped = ByteBuffer.wrap(value);
        int num = wrapped.getInt();
        return Float.toString((float) num);
    }

    /**
     * Converts the value to a hexadecimal string.
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
     * Return the value incremented by 1.
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

    /**
     * Hashes the value.
     * @return the hash as an int
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    /**
     * Compares this value to another value. Signed version.
     * @param comparator the value to compare to
     * @return true if this value is lower than the comparator
     */
    public boolean lowerThan(Value comparator) {
        BigInteger thisValue = new BigInteger(this.getByteValue());
        BigInteger comparatorValue = new BigInteger(comparator.getByteValue());
        return thisValue.compareTo(comparatorValue) < 0;
    }

    /**
     * Compares this value to another value. Unsigned version.
     * @param comparator the value to compare to
     * @return true if this value is lower than the comparator
     */
    public boolean lowerThanUnsigned(Value comparator) {
        byte[] valueUnsigned = new byte[value.length + 1];
        System.arraycopy(value, 0, valueUnsigned, 1, value.length);
        byte[] compUnsigned = new byte[value.length + 1];
        System.arraycopy(comparator.getByteValue(), 0, compUnsigned, 1, comparator.getByteValue().length);
        BigInteger thisValue = new BigInteger(valueUnsigned);
        BigInteger comparatorValue = new BigInteger(compUnsigned);
        return thisValue.compareTo(comparatorValue) < 0;
    }

    /**
     * Compares this value to a float value.
     * @param comparator the value to compare to
     * @return true if this value is lower than the comparator
     */
    public boolean lowerThanFloat(Value comparator) {
        ByteBuffer wrapped = ByteBuffer.wrap(comparator.getByteValue());
        int num = wrapped.getInt();
        float other = (float) num;
        wrapped = ByteBuffer.wrap(getByteValue());
        num = wrapped.getInt();
        float me = (float) num;
        return me < other;
    }

    /**
     * Compares this value to another value. Signed version.
     * @param comparator the value to compare to
     * @return true if this value is greater than the comparator
     */
    public boolean greaterThan(Value comparator) {
        BigInteger thisValue = new BigInteger(this.getByteValue());
        BigInteger comparatorValue = new BigInteger(comparator.getByteValue());
        return thisValue.compareTo(comparatorValue) > 0;
    }

    /**
     * Compares this value to another value. Unsigned version.
     * @param comparator the value to compare to
     * @return true if this value is greater than the comparator
     */
    public boolean greaterThanUnsigned(Value comparator) {
        byte[] valueUnsigned = new byte[value.length + 1];
        System.arraycopy(value, 0, valueUnsigned, 1, value.length);
        byte[] compUnsigned = new byte[value.length + 1];
        System.arraycopy(comparator.getByteValue(), 0, compUnsigned, 1, comparator.getByteValue().length);
        BigInteger thisValue = new BigInteger(valueUnsigned);
        BigInteger comparatorValue = new BigInteger(compUnsigned);
        return thisValue.compareTo(comparatorValue) > 0;
    }

    /**
     * TODO Noch nicht implementiert
     * Compares this value to a float value.
     * @param comparator the value to compare to
     * @return true if this value is greater than the comparator
     */
    public boolean greaterThanFloat(Value comparator) {
        ByteBuffer wrapped = ByteBuffer.wrap(comparator.getByteValue());
        int num = wrapped.getInt();
        float other = (float) num;
        wrapped = ByteBuffer.wrap(getByteValue());
        num = wrapped.getInt();
        float me = (float) num;

        return me > other;
    }

    /**
     * Converts the value to its negative.
     * @return the negative value
     */
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
