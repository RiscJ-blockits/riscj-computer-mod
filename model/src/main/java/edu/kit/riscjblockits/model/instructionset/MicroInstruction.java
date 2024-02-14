package edu.kit.riscjblockits.model.instructionset;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract class for common attributes and functionality of micro instructions.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public abstract class MicroInstruction implements IExecutableMicroInstruction {

    /**
     * Pattern to match the register part of a micro instruction.
     */
    protected static final Pattern REGISTER_PATTERN = Pattern.compile("(?<isregister>&)?(?<isFloat>f)?(?<name>\\[\\w+])");

    /**
     * The register(s) to read from.
     */
    private final String[] from;

    /**
     * The register to write to.
     */
    private final String to;

    /**
     * Creates a new MicroInstruction with the given registers.
     * @param from The registers to read from.
     * @param to The register to write to.
     */
    protected MicroInstruction(String[] from, String to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Getter method for the registers to read from.
     * @return The registers to read from.
     */
    public String[] getFrom() {
        return from;
    }

    /**
     * Getter method for the register to write to.
     * @return The register to write to.
     */
    public String getTo() {
        return to;
    }

    /**
     * Clones the MicroInstruction and replaces replaceable fields with the given values.
     * @param argumentsInstructionMap The map of arguments to instruction names.
     * @param intRegisters The map of integer registers.
     * @param floatRegisters The map of float registers.
     * @return The cloned MicroInstruction with the replaceable fields replaced.
     */
    public MicroInstruction getFilled(Map<String, String> argumentsInstructionMap,
                                      HashMap<Integer, String> intRegisters, HashMap<Integer, String> floatRegisters) {
        String[] filledFrom = new String[from.length];
        for (int i = 0; i < from.length; i++) {
            filledFrom[i] = getFilledExecutionPart(from[i], argumentsInstructionMap, intRegisters, floatRegisters);
        }
        String filledTo = getFilledExecutionPart(to, argumentsInstructionMap, intRegisters, floatRegisters);
        return clone(filledFrom, filledTo);
    }

    protected String getFilledExecutionPart(String part, Map<String, String> argumentsInstructionMap,
                                            HashMap<Integer, String> intRegisters, HashMap<Integer, String> floatRegisters) {
        Matcher matcher = REGISTER_PATTERN.matcher(part);
        // not fillable
        if (!matcher.matches()) {
            return part;
        }

        String name = matcher.group("name");
        String filled = argumentsInstructionMap.get(name);
        if (matcher.group("isregister") != null) {
            if (matcher.group("isFloat") != null) {
                return floatRegisters.get(Integer.parseInt(filled, 2));
            } else {
                return intRegisters.get(Integer.parseInt(filled, 2));
            }
        } else {
            return filled;
        }
    }
}
