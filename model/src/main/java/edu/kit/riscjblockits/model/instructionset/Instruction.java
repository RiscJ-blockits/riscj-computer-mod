package edu.kit.riscjblockits.model.instructionset;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents an instruction of the instruction set.
 * [JavaDoc in this class with minor support by GitHub Copilot]
 */
public class Instruction implements IQueryableInstruction {

    private static final Pattern ARGUMENT_TRANSLATION_PATTERN =
        Pattern.compile("(?<argument>\\[\\w+\\])<(?<length>\\d+(:?\\d+)?)>");
    private static final Pattern ARGUMENT_TRANSLATION_PATTERN_RANGE =
        Pattern.compile("(?<argument>\\[\\w+\\])<(?<from>\\d+):(?<to>\\d+)>");

    /**
     * The arguments of the instruction.
     */
    private final String[] arguments;

    /**
     * The opcode of the instruction.
     */
    private final String opcode;

    /**
     * The execution of the instruction as a sequence of micro instructions.
     */
    private final MicroInstruction[] execution;

    /**
     * The translation of the instruction to binary.
     */
    private final String[] translation;

    public Instruction(String[] arguments, String opcode, MicroInstruction[] execution, String[] translation) {
        this.arguments = arguments;
        this.opcode = opcode;
        this.execution = execution;
        this.translation = translation;
    }

    public Instruction(Instruction instruction, String binary) {
        this.arguments = instruction.arguments.clone();
        this.opcode = instruction.opcode;
        this.translation = instruction.translation.clone();

        this.execution = new MicroInstruction[instruction.execution.length];

        // generate argument hashmap
        Map<String, String> argumentsInstructionMap = generateArgumentInstructionMap(binary);

        // generate filled micro instructions
        for (int i = 0; i < instruction.execution.length; i++) {
            MicroInstruction microInstruction = instruction.execution[i];
            String[] from = microInstruction.getFrom();
            String to = microInstruction.getTo();
            for (int j = 0; j < from.length; j++) {
                if (argumentsInstructionMap.containsKey(from[j])) {
                    from[j] = argumentsInstructionMap.get(from[j]);
                }
            }
            to = argumentsInstructionMap.get(to);

            this.execution[i] = instruction.execution[i].clone(from, to);
        }
    }

    private Map<String, String> generateArgumentInstructionMap(String binary) {
        Map<String, String> argumentsInstructionMap = new HashMap<>();
        int binaryPosition = 0;
        for (int i = 0; i < translation.length; i++) {
            Matcher matcher = ARGUMENT_TRANSLATION_PATTERN.matcher(translation[i]);
            Matcher matcherRange = ARGUMENT_TRANSLATION_PATTERN_RANGE.matcher(translation[i]);
            if (matcherRange.matches()) {
                // TODO catch
                int to = Integer.parseInt(matcherRange.group("to"));
                int from = Integer.parseInt(matcherRange.group("from"));

                if (from > to) {
                    to += from;
                    from = to - from;
                    to -= from;
                }

                String argument = matcherRange.group("argument");

                String current = "123456700abc";
                if (argumentsInstructionMap.containsKey(argument)) {
                    current = argumentsInstructionMap.get(argument);
                }

                int l = current.length();
                if (l < to) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(binary.substring(binaryPosition, binaryPosition + to - from + 1));
                    sb.append("0".repeat(Math.max(0, from - l)));
                    sb.append(current);
                    argumentsInstructionMap.put(argument, sb.toString());
                    binaryPosition += to - from;
                    continue;
                }
                StringBuilder sb = new StringBuilder();
                sb.append(current.substring(0, l - to - 1));
                sb.append(binary.substring(binaryPosition, binaryPosition + to - from + 1));
                sb.append(current.substring(l - from));

                argumentsInstructionMap.put(argument, sb.toString());
                binaryPosition += to - from;
                continue;
            }
            else if (matcher.matches()) {
                String argument = matcher.group("argument");
                int length = Integer.parseInt(matcher.group("length"));
                argumentsInstructionMap.put(argument, binary.substring(binaryPosition, binaryPosition + length));
                binaryPosition += length;
                continue;
            }
            binaryPosition += translation.length;
        }
        return argumentsInstructionMap;
    }


    /**
     * Getter for the opcode of the instruction.
     *
     * @return The opcode of the instruction as a string.
     */
    public String getOpcode() {
        return opcode;
    }


    /**
     * Getter for the binary translation of the instruction.
     *
     * @return The binary translation of the instruction as a string array.
     */
    public String[] getTranslation() {
        return translation;
    }

    /**
     * Getter for the arguments of the instruction.
     *
     * @return The arguments of the instruction as a string array.
     */
    public String[] getArguments() {
        return arguments;
    }

    /**
     * Getter for the execution of the instruction.
     *
     * @return The execution of the instruction as a sequence of micro instructions.
     */
    public MicroInstruction[] getExecution() {
        return execution;
    }


}
