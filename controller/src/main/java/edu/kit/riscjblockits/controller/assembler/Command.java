package edu.kit.riscjblockits.controller.assembler;

import edu.kit.riscjblockits.model.Value;
import edu.kit.riscjblockits.model.instructionset.IQueryableInstruction;
import edu.kit.riscjblockits.model.instructionset.Instruction;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a single command in the assembly code.
 */
public class Command {
    /**
     * regex pattern to match argument translation parts
     */
    private static final Pattern ARGUMENT_TRANSLATION_PATTERN = Pattern.compile("(?<argument>\\[\\w+\\])<(?<length>\\d+)>");


    /**
     * the assembled translation of the command
     */
    private final String[] assembledTranslation;

    /**
     * map of arguments to their values
     */
    private final Map<String, String> argumentsInstructionMap = new HashMap<>();

    /**
     * Constructor for a command
     * @param instruction the instruction of the command
     * @param arguments the arguments of the command
     * @throws AssemblyException if the given command cant be assembled
     */
    public Command(IQueryableInstruction instruction, String[] arguments) throws AssemblyException {
        for (int i = 0; i < arguments.length; i++) {
            argumentsInstructionMap.put(instruction.getArguments()[i], arguments[i]);
        }
        String[] translation = instruction.getTranslation();

        assembledTranslation = new String[translation.length];
        for (int i = 0; i < translation.length; i++) {
            assembledTranslation[i] = assembleArgument(translation[i]);
        }



    }

    /**
     * Assembles a single translation part
     * @param translationPart the translation part to assemble
     * @return the assembled translation part
     * @throws AssemblyException if the translation part cant be assembled
     */
    private String assembleArgument(String translationPart) throws AssemblyException {
        // check if translation part is argument
        Matcher matcher = ARGUMENT_TRANSLATION_PATTERN.matcher(translationPart);
        if (matcher.matches()) {
            String argument = matcher.group("argument");
            int length = Integer.parseInt(matcher.group("length"));

            // get value from arguments map
            String argumentValue = argumentsInstructionMap.get(argument);
            if (argumentValue == null) {
                throw new AssemblyException("Argument " + argument + " not found --> invalid Instruction-Set");
            }
            // extract value from argument
            Value argumentValueObject = ValueExtractor.extractValue(argumentValue, length);
            if (argumentValueObject == null) {
                throw new AssemblyException("Argument " + argument + " has invalid value");
            }
            String argumentValueBinary = argumentValueObject.getBinaryValue();
            // cut off leading zeros --> trim to arguments length
            return argumentValueBinary.substring(argumentValueBinary.length() - length);
        }

        return translationPart;
    }

    /**
     * @return the assembled translation of the command
     */
    public Value asValue() {
        StringBuilder binary = new StringBuilder();
        for (String s : assembledTranslation) {
            binary.append(s);
        }
        String binaryString = binary.toString();
        return Value.fromBinary(binaryString, binaryString.length()/8 + (binaryString.length()%8 > 0 ? 1 : 0));
    }
}
