package edu.kit.riscjblockits.controller.assembler;

import edu.kit.riscjblockits.model.memoryrepresentation.Value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides parsing of different number values into {@link Value} objects.
 */
public final class ValueExtractor {

    /**
     * regex pattern to match hex values.
     */
    private static final Pattern HEX_VALUE_PATTERN = Pattern.compile("(?<negative>-)?0x(?<value>[0-9a-fA-F]+)");
    /**
     * regex pattern to match decimal values.
     */
    private static final Pattern DEC_VALUE_PATTERN = Pattern.compile("(?<negative>-)?(?<value>\\d+)");
    /**
     * regex pattern to match binary values.
     */
    private static final Pattern BIN_VALUE_PATTERN = Pattern.compile("(?<negative>-)?0b(?<value>[01]+)");

    /**
     * Utility class, no instances should be created.
     * @throws IllegalStateException if an instance is created.
     */
    private ValueExtractor() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Extracts a value from a string.
     * can extract hexadecimal (0x), binary (0b) or decimal values
     * @param value the string to extract the value from
     * @param length the length of the value in bytes
     * @return the extracted value, null if the value cant be extracted
     */
    public static Value extractValue(String value, int length) {
        Matcher matcher = HEX_VALUE_PATTERN.matcher(value);
        if (matcher.matches()) {
            if (matcher.group("negative") != null) {
                return Value.fromHex(matcher.group("value"), length).negate();
            }
            return Value.fromHex(matcher.group("value"), length);
        }
        matcher = DEC_VALUE_PATTERN.matcher(value);
        if (matcher.matches()) {
            if (matcher.group("negative") != null) {
                return Value.fromDecimal(matcher.group("value"), length).negate();
            }
            return Value.fromDecimal(matcher.group("value"), length);
        }
        matcher = BIN_VALUE_PATTERN.matcher(value);
        if (matcher.matches()) {
            if (matcher.group("negative") != null) {
                return Value.fromBinary(matcher.group("value"), length).negate();
            }
            return Value.fromBinary(matcher.group("value"), length);
        }
        return null;
    }
}
