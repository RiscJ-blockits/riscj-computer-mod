package edu.kit.riscjblockits.controller.assembler;

import edu.kit.riscjblockits.model.memoryRepresentation.Value;

import java.util.regex.Pattern;

/**
 * This class provides parsing of different number values into {@link Value} objects
 */
public class ValueExtractor {
    /**
     * regex pattern to match hex values
     */
    private static final Pattern HEX_VALUE_PATTERN = Pattern.compile("0x[0-9a-fA-F]+");
    /**
     * regex pattern to match decimal values
     */
    private static final Pattern DEC_VALUE_PATTERN = Pattern.compile("\\d+");
    /**
     * regex pattern to match binary values
     */
    private static final Pattern BIN_VALUE_PATTERN = Pattern.compile("0b[01]+");

    /**
     * Extracts a value from a string.
     * can extract hexadecimal (0x), binary (0b) or decimal values
     * @param value the string to extract the value from
     * @param length the length of the value in bytes
     * @return the extracted value, null if the value cant be extracted
     */
    public static Value extractValue(String value, int length) {
        if (HEX_VALUE_PATTERN.matcher(value).matches()) {
            return Value.fromHex(value.replace("0x", ""), length);
        } else if (DEC_VALUE_PATTERN.matcher(value).matches()) {
            return Value.fromDecimal(value, length);
        } else if (BIN_VALUE_PATTERN.matcher(value).matches()) {
            return Value.fromBinary(value.replace("0b", ""), length);
        }
        return null;
    }
}
