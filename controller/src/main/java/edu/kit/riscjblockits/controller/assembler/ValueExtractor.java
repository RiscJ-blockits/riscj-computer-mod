package edu.kit.riscjblockits.controller.assembler;

import edu.kit.riscjblockits.model.Value;

import java.util.regex.Pattern;

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
     * Extracts a value from a string
     * @param value the string to extract the value from
     * @param length the length of the value in bits
     * @return the extracted value, null if the value cant be extracted
     */
    public static Value extractValue(String value, int length) {
        String argumentValueBinary = null;
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
